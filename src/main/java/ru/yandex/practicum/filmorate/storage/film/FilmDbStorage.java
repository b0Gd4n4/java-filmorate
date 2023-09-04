package ru.yandex.practicum.filmorate.storage.film;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.marker.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.Constants.STATUS_ACTIVE;
import static ru.yandex.practicum.filmorate.storage.Constants.STATUS_DELETED;

@Slf4j
@Component
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private static final String FILM_LOG = "FILM - {} : {}";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public Film addFilm(@NonNull Film film) {
        ValidateService.validateFilm(film);
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, rate) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate()
        );
        log.info(FILM_LOG, LocalDateTime.now(), "added");
        List<Film> filmsList = jdbcTemplate.query("SELECT f.ID, f.name, description, release_date, duration, rate, deleted, " +
                        "fm.MPA_ID, m.NAME as mpa_name FROM films f " +
                        "LEFT JOIN (SELECT * FROM FILM_MPA WHERE status_id = 2) fm ON f.ID = fm.FILM_ID " +
                        "LEFT JOIN MPA m ON m.ID = fm.MPA_ID " +
                        "ORDER BY f.ID DESC LIMIT 1",
                new FilmMapper()
        );
        Film filmToReturn = filmsList.get(0);
        if (film.getMpa() != null && film.getMpa().getId() != null) {
            addMpaById(film.getMpa().getId(), Math.toIntExact(filmToReturn.getId()));
        }
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            film.getGenres().forEach(genre -> addGenreById(genre.getId(), Math.toIntExact(filmToReturn.getId())));
        }
        return getFilmById(Math.toIntExact(filmToReturn.getId()));
    }

    @Override
    public Film updateFilm(@NonNull Integer id, @NonNull Film film) {
        getFilmById(id);
        ValidateService.validateFilm(film);
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, rate = ?, deleted = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getDeleted(),
                id
        );
        if (film.getMpa() != null && film.getMpa().getId() != null) {
            addMpaById(film.getMpa().getId(), Math.toIntExact(film.getId()));
        }
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            removeAllGenres(id);
            film.getGenres().forEach(genre -> addGenreById(genre.getId(), id));
        } else {
            removeAllGenres(id);
        }
        log.info(FILM_LOG, LocalDateTime.now(), "updated");
        return getFilmById(id);
    }

    @Override
    public Film getFilmById(@NonNull Integer id) {
        List<Film> filmsList = jdbcTemplate.query("SELECT f.ID, f.name, description, release_date, duration, rate, deleted, " +
                        "fm.MPA_ID, m.NAME as mpa_name FROM films f " +
                        "LEFT JOIN (SELECT * FROM FILM_MPA WHERE status_id = 2) fm ON f.ID = fm.FILM_ID " +
                        "LEFT JOIN MPA m ON m.ID = fm.MPA_ID " +
                        "WHERE f.ID = " + id,
                new FilmMapper()
        );
        if (filmsList.isEmpty()) {
            log.info("Film not found, id = {}", id);
            throw new NotFoundException("Film with id = " + id + " do not exist");
        }
        log.info("Found film with id = {}", id);
        Film film = filmsList.get(0);
        film.setGenres(genreDbStorage.getGenresOfFilm(id));
        return film;
    }

    @Override
    public Set<Film> getAllFilms() {
        List<Film> filmsList = jdbcTemplate.query("SELECT f.ID, f.name, description, release_date, duration, rate, deleted, " +
                        "fm.MPA_ID, m.NAME as mpa_name FROM films f " +
                        "LEFT JOIN (SELECT * FROM FILM_MPA WHERE status_id = 2) fm ON f.ID = fm.FILM_ID " +
                        "LEFT JOIN MPA m ON m.ID = fm.MPA_ID " +
                        "ORDER BY f.ID",
                new FilmMapper()
        );
        if (filmsList.isEmpty()) {
            log.info("No films found in database");
        }
        log.info("Total films found: {}", filmsList.size());
        filmsList.forEach(film -> film.setGenres(genreDbStorage.getGenresOfFilm(Math.toIntExact(film.getId()))));
        return filmsList.stream()
                .sorted(Film::getFilmIdToCompare)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    @Override
    public void addGenreById(Integer genreId, Integer filmId) {
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet("SELECT * FROM film_genre WHERE (film_id = ? AND genre_id = ?) AND status_id = ?",
                filmId,
                genreId,
                STATUS_ACTIVE
        );
        if (resultSet.next()) {
            log.info("Genre already added to film with id = {}", filmId);
        } else {
            String sqlQuery = "INSERT INTO film_genre (film_id, genre_id, status_id) " +
                    "VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    filmId,
                    genreId,
                    STATUS_ACTIVE
            );
            log.info("Add new genre to film with id = {}", filmId);
        }
    }

    @Override
    public void removeGenreById(Integer genreId, Integer filmId) {
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet("SELECT * FROM film_genre WHERE (film_id = ? AND genre_id = ?) AND status_id = ?",
                filmId,
                genreId,
                STATUS_ACTIVE
        );
        if (resultSet.next()) {
            String sqlQuery = "UPDATE film_genre SET status_id = ? WHERE (film_id = ? AND genre_id = ?)";
            jdbcTemplate.update(sqlQuery,
                    STATUS_DELETED,
                    filmId,
                    genreId
            );
            log.info("Genre was removed from film with id = {}", filmId);
        } else {
            log.info("Genre was not added to film with id = {}", filmId);
        }
    }

    @Override
    public void removeAllGenres(Integer filmId) {
        String sqlQuery = "UPDATE film_genre SET status_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                STATUS_DELETED,
                filmId
        );
        log.info("All genres was removed from film with id = {}", filmId);
    }

    @Override
    public void addMpaById(Integer mpaId, Integer filmId) {
        removeMpa(filmId);
        String sqlQuery = "INSERT INTO film_mpa (film_id, mpa_id, status_id) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                mpaId,
                STATUS_ACTIVE
        );
        log.info("Add new mpa to film with id = {}", filmId);
    }

    @Override
    public void removeMpa(Integer filmId) {
        String sqlQuery = "UPDATE film_mpa SET status_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                STATUS_DELETED,
                filmId
        );
        log.info("Mpa was removed from film with id = {}", filmId);
    }
}

