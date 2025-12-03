package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exeptions.ValidExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmTest {

    FilmController filmController = new FilmController();

    @Test
    public void nullName() {

        Film film = new Film(1, null, "dsdsds", LocalDate.of(2000, 2, 23), Duration.ofMinutes(232));
        final ValidExeption exception = assertThrows(

                ValidExeption.class,

                new Executable() {
                    @Override
                    public void execute() {
                        filmController.createFilm(film);
                    }
                });

        System.out.println(exception.getMessage());

        assertEquals("filmName IS NULL", exception.getMessage());
    }

    @Test
    public void lengthName() {

        Film film = new Film(1, "one", "ffffffffffffffffffffffffffffffffffff" +
                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", LocalDate.of(2000, 2, 23), Duration.ofMinutes(232));

        final ValidExeption exception = assertThrows(

                ValidExeption.class,

                new Executable() {
                    @Override
                    public void execute() {
                        filmController.createFilm(film);
                    }
                });

        System.out.println(exception.getMessage());

        assertEquals("film.length > 200", exception.getMessage());
    }

    @Test
    public void releaseDateIsBefore() {

        Film film = new Film(1, "null", "dsdsds", LocalDate.of(1985, 12, 28), Duration.ofMinutes(232));
        final ValidExeption exception = assertThrows(

                ValidExeption.class,

                new Executable() {
                    @Override
                    public void execute() {
                        filmController.createFilm(film);
                    }
                });

        System.out.println(exception.getMessage());

        assertEquals("ReleaseDate isBefore 1985-12-28", exception.getMessage());
    }

    @Test
    public void durationIsNegative() {

        Film film = new Film(1, "null", "dsdsds", LocalDate.of(1985, 12, 29), Duration.ofMinutes(-1));
        final ValidExeption exception = assertThrows(

                ValidExeption.class,

                new Executable() {
                    @Override
                    public void execute() {
                        filmController.createFilm(film);
                    }
                });

        System.out.println(exception.getMessage());

        assertEquals("getDuration().isNegative()", exception.getMessage());
    }
}
