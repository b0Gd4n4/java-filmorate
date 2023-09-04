package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceTest {

    private final UserService userService;
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Test
    @DirtiesContext
    void getFilm_withNormalBehavior() {
        Film filmTest = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();
        filmService.addFilm(filmTest);

        Optional<Film> filmOptional = Optional.ofNullable(filmService.getFilmById(Math.toIntExact(filmTest.getId())));

        assertTrue(filmOptional.isPresent());
        Film film = filmOptional.get();
        assertEquals(1, film.getId());
        assertEquals("name", film.getName());
        assertEquals("description", film.getDescription());
        assertEquals(LocalDate.of(2012, 1, 1), film.getReleaseDate());
        assertEquals(90, film.getDuration());
        assertEquals(4, film.getRate());
    }

    @Test
    @DirtiesContext
    void getFilm_withWrongId() {
        Film filmTest = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();
        filmService.addFilm(filmTest);
        int wrongId = 2;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            filmService.getFilmById(wrongId);
        });

        assertEquals("film с id=" + wrongId + "не найден", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void getAllFilms_withNormalBehavior() {
        Film filmTest = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();
        filmService.addFilm(filmTest);

        Film filmTest1 = Film.builder()
                .id(2L)
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();
        filmService.addFilm(filmTest1);

        Collection<Film> films = filmService.getAllFilms();

        assertEquals(2, films.size());
    }

    @Test
    @DirtiesContext
    void addFilm_withNormalBehavior() {
        Film filmTest = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();
        filmService.addFilm(filmTest);

        Optional<Film> filmOptional = Optional.ofNullable(filmService.getFilmById(Math.toIntExact(filmTest.getId())));

        assertTrue(filmOptional.isPresent());
        Film film = filmOptional.get();
        assertEquals(1, film.getId());
        assertEquals("name", film.getName());
        assertEquals("description", film.getDescription());
        assertEquals(LocalDate.of(2012, 1, 1), film.getReleaseDate());
        assertEquals(90, film.getDuration());
        assertEquals(4, film.getRate());
    }


    @Test
    @DirtiesContext
    void addFilm_withWrongReleaseDate() {
        Film filmTest = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1894, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmService.addFilm(filmTest);
        });

        assertEquals("Дата релиза не может быть раньше 28 Декабря 1895.", exception.getMessage());
    }


    @Test
    @DirtiesContext
    void updateFilm_withNormalBehavior() {
        Film filmTest = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();
        filmService.addFilm(filmTest);

        Film filmTest1 = Film.builder()
                .id(1L)
                .name("new name")
                .description("new description")
                .releaseDate(LocalDate.of(2011, 1, 1))
                .duration(80)
                .rate(3)
                .mpa(new MPA(4, "R")).build();
        filmService.updateFilm(filmTest1);

        Optional<Film> filmOptional = Optional.ofNullable(filmService.getFilmById(Math.toIntExact(filmTest.getId())));

        assertTrue(filmOptional.isPresent());
        Film film = filmOptional.get();
        assertEquals("new name", film.getName());
        assertEquals("new description", film.getDescription());
        assertEquals(LocalDate.of(2011, 1, 1), film.getReleaseDate());
        assertEquals(80, film.getDuration());
        assertEquals(3, film.getRate());
    }

    @Test
    @DirtiesContext
    void updateFilm_withWrongId() {
        Film filmTest = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();
        filmService.addFilm(filmTest);

        Film filmTest1 = Film.builder()
                .id(3L)
                .name("new name")
                .description("new description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            filmService.updateFilm(filmTest1);
        });

        assertEquals("film с id=" + filmTest1.getId() + "не найден", exception.getMessage());
    }


    @Test
    @DirtiesContext
    void getPopularFilms_withNormalBehavior() {
        Film filmTest = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .duration(90)
                .rate(4)
                .mpa(new MPA(4, "R")).build();
        filmService.addFilm(filmTest);

        User userTest = User.builder()
                .id(1L)
                .email("awb@mail.ru")
                .login("awb")
                .name("Alex")
                .birthday(LocalDate.of(1996, 8, 9)).build();
        userService.createUser(userTest);
        filmService.makeLike(Math.toIntExact(filmTest.getId()), Math.toIntExact(userTest.getId()));

        Collection<Film> films = filmService.getPopularFilms(1);

        assertEquals(1, films.size());
    }

    @Test
    @DirtiesContext
    void getPopularFilms_withWrongCount() {
        int wrongCount = -1;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            filmService.getPopularFilms(wrongCount);
        });

        assertEquals("count не может быть отрицательным.", exception.getMessage());
    }
}