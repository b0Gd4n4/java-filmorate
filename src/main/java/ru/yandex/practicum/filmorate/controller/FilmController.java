package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;


    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен новый фильм");
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Validated({Marker.Update.class}) @RequestBody Film film) {
        log.info("Запрос на изменение фильма. Фильм изменён.");

        return filmService.updateFilm(film);
    }


    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }


    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable Long id) {
        return filmService.findFilmById(id);
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
    public List<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        return filmService.getTopFilms(count);
    }


}