package controller;

import exceptions.FilmException;
import exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import model.Film;
import org.springframework.web.bind.annotation.*;

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


    final Map<Integer, Film> films = new HashMap<>();

    public FilmController() {
        this.filmIdGenerator = new FilmIdGenerator();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        isValid(film);
        int id = filmIdGenerator.getNextFreeId();
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен новый фильм");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
            if (films.get(film.getId()) != null) {
                isValid(film);
                films.put(film.getId(), film);
                log.info("Запрос на изменение фильма. Фильм изменён.");
            } else {
                log.error("Запрос на изменение фильма. Фильм не найден.");
                throw new FilmException("Фильм не найден.");
            }
            return film;
        }


    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void isValid(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Некорректно указана дата релиза.");
        }
    }

    public static final class FilmIdGenerator {
        private int nextFreeId = 0;


        public int getNextFreeId() {
            return nextFreeId++;
        }


    }
}