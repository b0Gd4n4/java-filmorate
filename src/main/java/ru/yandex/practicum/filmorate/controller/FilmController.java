package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@RequiredArgsConstructor
public class FilmController {

    private final FilmIdGenerator filmIdGenerator;

    final Map<Long, Film> films = new HashMap<>();


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
    public Film updateFilm(@Validated({Marker.Update.class}) @RequestBody Film film) {

        if (!films.containsKey(film.getId())) {
            throw new DataNotFoundException("Ключ не найден: " + film.getId());
        } else {
            validate(film);
            films.put(film.getId(), film);
            log.info("Запрос на изменение фильма. Фильм изменён.");
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

    @Component
    public static final class FilmIdGenerator {
        private long nextFreeId = 0;


        public Long getNextFreeId() {
            return ++nextFreeId;
        }


    }

}