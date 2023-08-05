package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Long getNextFreeId();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(Long id);




}
