package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface FilmStorage {


    Film getFilm(Long id) throws SQLException;

    Collection<Film> getAllFilms() throws SQLException;

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void makeLike(int idFilm, int idUser);

    void deleteLike(int idFilm, int idUser);

    Collection<Film> getPopularFilms(int count) throws SQLException;

    List<Integer> getFilmLikes(Integer id);

    MPA checkMpa(Film film);

    List<Genre> getGenres(int idFilm);

    List<Genre> checkGenre(Film film);

    void insertFilmGenres(Film film);

    void deleteFilmGenres(Film film);
}
