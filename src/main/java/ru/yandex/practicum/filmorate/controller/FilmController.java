package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/film")
    public void createFilm(@RequestBody Film film) {

            if(film.getName() == null) {
                throw new ValidExeption("filmName IS NULL");
            }

            if (film.getDescription().length() > 200) {
                throw new ValidExeption("film.length > 200");
            }

            if (!film.getReleaseDate().isAfter(LocalDate.of(1985, 12, 28))) {
                throw new ValidExeption("ReleaseDate isBefore 1985-12-28");
            }

            if (film.getDuration().isNegative()) {
                throw new ValidExeption("getDuration().isNegative()");
            }
            films.put(inrIds(film.getId()), film);
            log.info("Фильм Добавлен");

    }

    @PutMapping("/film")
    public void updateFilm(@RequestBody Film Film) {
        films.put(Film.getId(), Film);
        log.info("Фильм обнавлен");
    }

    public int inrIds(int i) {
        return i++;
    }
}
