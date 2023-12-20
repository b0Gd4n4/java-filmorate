package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {

    public static void validateFilm(Film film) {
        try {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.info("Дата релиза не может быть раньше 28 Декабря 1895.");
                throw new ValidationException("Дата релиза не может быть раньше 28 Декабря 1895.");
            }

        } catch (NullPointerException ex) {
            log.info("Поля фильма не могут быть null.");
            throw new ValidationException("Поля фильма не могут быть null.");
        }
    }
}