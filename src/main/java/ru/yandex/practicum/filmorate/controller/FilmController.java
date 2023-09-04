package ru.yandex.practicum.filmorate.controller;

/*import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.marker.Marker;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;


    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен новый фильм {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Validated({Marker.Update.class}) @RequestBody Film film) {
        log.info("Запрос на изменение фильма {}. Фильм изменён.", film);

        return filmService.updateFilm(film);
    }


    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов.");
        return filmService.getAllFilms();
    }


    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable Long id) {
        log.info("Запрос на получения фильма {} по id", id);
        return filmService.findFilmById(id);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
        log.info("Фильм с id={} лайкнул пользователь {}.", id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        log.info("Пользователь {} удалил лайк с фильма {}.", id, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Long count) {
        log.info("Получения списка топ {} фильмов по лайкам.", count);
        return filmService.getTopFilms(Math.toIntExact(count));
    }


}*/ // --------------------------- > 1

/*import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.getAll();
    }

    @GetMapping("/films/{id}")
    public Film findById(@PathVariable Long id) {
        return filmService.getById(id);
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        return filmService.add(film);
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws ValidationException, DataNotFoundException {
        return filmService.update(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getTop(@RequestParam(required = false) Integer count) {
        return filmService.getTop(count);
    }

}*/ //------------------------------- > 2

/*import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Get all films");
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (filmService.checkDate(film)) {
            log.info(String.format("Film %s was added", film.getName()));
            return filmService.addFilm(film);
        } else {
            throw new ValidationException("Wrong release date");
        }
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info(String.format("Get film %d", id));
        return filmService.getFilmById(id);
    }

    @PutMapping("/films")
    public Film changeFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (filmService.checkDate(film)) {
            log.info(String.format("Film %d was changed", film.getId()));
            return filmService.changeFilm(film);
        } else {
            throw new ValidationException("Wrong release date");
        }
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void like(@PathVariable long id, @PathVariable long userId) {
        log.info(String.format("User %d likes film %d", userId, id));
        filmService.like(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info(String.format("User %d deleted like from film %d", userId, id));
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Get popular films");
        return filmService.getPopularFilms(count);
    }
}*/ //3

/*import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> get() {
        return filmService.getAll();
    }

    @GetMapping (path = "/{id}")
    public Film getByID(@PathVariable ("id") int filmID) {
        return filmService.getByID(filmID);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping(path = "/{id}/like/{userID}")
    public void addLike(@PathVariable ("id") int filmID,
                        @PathVariable int userID) {
        filmService.addLike(filmID, userID);
    }

    @DeleteMapping(path = "/{id}/like/{userID}")
    public void removeLike(@PathVariable ("id") int filmID,
                           @PathVariable int userID) {
        filmService.removeLike(filmID, userID);
    }

    @GetMapping(path = "/popular")
    public List<Film> getPopular(@RequestParam (defaultValue = "10") @Positive int count) {
        return filmService.getPopular(count);
    }
}*/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmDbStorage filmStorage;
    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Received GET request: new film");
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Received PUT request: update film {}", film.getId());
        return filmStorage.updateFilm(Math.toIntExact(film.getId()), film);
    }

    @GetMapping
    public Set<Film> findAllFilms() {
        log.info("Received GET request: all films");
        return filmStorage.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Received PUT request: add like to film {} from user {}", id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Received DELETE request: remove like of film {} form user {}", id, userId);
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Received GET request: top {} popular films", count);
        return filmService.getTopFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) {
        log.info("Received GET request: film {}", id);
        return filmStorage.getFilmById(id);
    }
}