package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;


    private final int topFilms = 10;


    public Film createFilm(Film film) {
        validate(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validate(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film findFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }


    public Film addLike(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id);
        film.addLike(userId);
        filmStorage.updateFilm(film);
        log.info("Фильм с id={} лайкнул пользователь {}.", film.getId(), userId);
        return film;
    }

    public Film removeLike(Long id, Long userId) throws DataNotFoundException {
        Film film = filmStorage.getFilmById(id);
        boolean result = film.removeLike(userId);
        if (!result) {
            throw new DataNotFoundException("Лайк пользователя " + userId + " не найден");
        }
        filmStorage.updateFilm(film);
        log.info("Пользователь удалил лайк с фильма.");
        return film;
    }

    public List<Film> getTopFilms(Integer count) {
        if (count == null) {
            count = topFilms;
        }
        List<Film> films = filmStorage.getAllFilms();
        films.sort(Comparator.comparingInt(Film::numberOfLikes).reversed());
        return films.stream().limit(count).collect(Collectors.toList());
    }

    public void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Дата указана неккоректно.");
        }
    }


}