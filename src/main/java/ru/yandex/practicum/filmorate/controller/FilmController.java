package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.marker.Marker;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    private Collection<Film> getAllFilms() throws SQLException {
        log.info("Запрос на получение всех фильмов.");
        return filmService.getAllFilms();
    }

    @GetMapping(value = "/{id}")
    private Film getFilm(@PathVariable Long id) throws SQLException {
        log.info("Запрос на получения фильма {} по id", id);
        return filmService.getFilmById(id);
    }

    @GetMapping(value = "/popular")
    private Collection<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) throws SQLException {
        log.info("Получения списка топ {} фильмов по лайкам.", count);
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    private Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен новый фильм {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    private Film updateFilm(@Validated({Marker.Update.class}) @RequestBody Film film) throws SQLException {
        log.info("Запрос на изменение фильма {}. Фильм изменён.", film);
        return filmService.updateFilm(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    private void makeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Фильм с id={} лайкнул пользователь {}.", id, userId);
        filmService.makeLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    private void deleteLike(@PathVariable int id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
        log.info("Пользователь {} удалил лайк с фильма {}.", id, userId);
    }
}