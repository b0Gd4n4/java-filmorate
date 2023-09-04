package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.Set;

public interface FilmStorage {
    Film addFilm(Film film);


    Film updateFilm(Integer id, Film film);


    Film getFilmById(Integer id) throws SQLException;


    Set<Film> getAllFilms() throws SQLException;


    void addGenreById(Integer genreId, Integer filmId);


    void removeGenreById(Integer genreId, Integer filmId);

    void removeAllGenres(Integer filmId);

    void addMpaById(Integer mpaId, Integer filmId);

    void removeMpa(Integer filmId);
}
