package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.marker.Marker;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/films", produces = "application/json")

public class FilmController {
    private final FilmIdGenerator filmIdGenerator;


    final Map<Long, Film> films = new HashMap<>();

    public FilmController() {
        this.filmIdGenerator = new FilmIdGenerator();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validate(film);
        long id = filmIdGenerator.getNextFreeId();
        film.setId(id);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм");
        return film;
    }

    @PutMapping
    @Validated({Marker.Update.class})
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.get(film.getId()) != null) {
            validate(film);
            films.put(film.getId(), film);
            log.info("Запрос на изменение фильма. Фильм изменён.");
        } else {
            log.error("Запрос на изменение фильма. Фильм не найден.");
            throw new DataNotFoundException("Фильм не найден.");
        }
        return film;
    }


    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Дата указана неккоректно.");
        }
    }

    public static final class FilmIdGenerator {
        private int nextFreeId = 0;


        public int getNextFreeId() {
            return ++nextFreeId;
        }


    }
}