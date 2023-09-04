package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.FilmValidator.validateFilm;


@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage filmStorageDb;
    private final UserStorage userStorageDb;

    public Film getFilmById(int id) {
        Film film = filmStorageDb.getFilm(id);
        if (film == null) {
            throw new NotFoundException("film с id=" + id + "не найден");
        }
        List<Genre> genres = filmStorageDb.getGenres(id);
        film.setGenres(genres);
        return film;
    }

    public Collection<Film> getAllFilms() {
        return filmStorageDb.getAllFilms();
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        MPA mpa = filmStorageDb.checkMpa(film);
        List<Genre> genres = filmStorageDb.checkGenre(film);
        Film film1 = filmStorageDb.addFilm(film);
        film1.setMpa(mpa);
        film1.setGenres(genres);
        filmStorageDb.insertFilmGenres(film1);
        return film;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        getFilmById(Math.toIntExact(film.getId()));
        MPA mpa = filmStorageDb.checkMpa(film);
        List<Genre> genres = filmStorageDb.checkGenre(film);
        Film film1 = filmStorageDb.updateFilm(film);
        film1.setMpa(mpa);
        film1.setGenres(genres);
        filmStorageDb.deleteFilmGenres(film1);
        filmStorageDb.insertFilmGenres(film1);
        return film1;
    }

    public void makeLike(int idFilm, int idUser) {
        filmStorageDb.makeLike(idFilm, idUser);
    }

    public void deleteLike(int idFilm, Long idUser) {
        User user = userStorageDb.getUser(idUser);
        if (user == null) {
            throw new NotFoundException("User с id " + idUser + " не найден.");
        }
        filmStorageDb.deleteLike(idFilm, Math.toIntExact(idUser));
    }

    public Collection<Film> getPopularFilms(int count) {
        if (count < 1) {
            log.info("count не может быть отрицательным.");
            throw new NotFoundException("count не может быть отрицательным.");
        }
        return filmStorageDb.getPopularFilms(count);
    }
}

