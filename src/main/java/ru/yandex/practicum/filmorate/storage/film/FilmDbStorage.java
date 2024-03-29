package ru.yandex.practicum.filmorate.storage.film;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film getFilm(Long id) {
        String sql = "SELECT f.*, m.* FROM films f JOIN mpa m ON f.mpa_id = m.mpa_id WHERE f.film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        Film film = null;
        if (filmRows.next()) {
            MPA mpa = MPA.builder()
                    .id(filmRows.getLong("mpa_id"))
                    .name(filmRows.getString("mpa_name"))
                    .build();
            film = Film.builder()
                    .id((long) id)
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(filmRows.getDate("releaseDate").toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .rate(filmRows.getInt("rate"))
                    .mpa(mpa)
                    .build();

        }

        return film;
    }


    @Override
    public Collection<Film> getAllFilms() {
        String sqlFilms = "SELECT f.*, g.*, m.mpa_name " +
                "FROM films f " +
                "LEFT OUTER JOIN film_genres fg ON fg.film_id = f.film_id " +
                "LEFT OUTER JOIN genres g ON g.genre_id = fg.genre_id " +
                "LEFT OUTER JOIN mpa m ON m.mpa_id = f.mpa_id";

        Map<Integer, Film> filmMap = new HashMap<>();
        jdbcTemplate.query(sqlFilms, rs -> {
            int id = rs.getInt("film_id");
            Film film = filmMap.get(id);
            if (film == null) {
                film = Film.builder()
                        .id((long) id)
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("releaseDate").toLocalDate())
                        .duration(rs.getInt("duration"))
                        .rate(rs.getInt("rate"))
                        .mpa(FilmDbStorage.makeMpa(rs, 0))
                        .genres(new ArrayList<>())
                        .build();
                filmMap.put(id, film);
            }

            Integer genreId = rs.getInt("genre_id");
            if (genreId != null && genreId != 0) {
                Genre genre = FilmDbStorage.makeGenre(rs, 0);
                film.getGenres().add(genre);
            }
        });
        return filmMap.values();
    }


    @Override
    public Film addFilm(Film film) {
        String sqlInsertFilm = "INSERT INTO films (name, description, releaseDate, duration, rate, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsertFilm, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate() != null ? film.getRate() : 0);
            ps.setLong(6, film.getMpa().getId());
            return ps;
        }, keyHolder);
        int filmId = keyHolder.getKey().intValue();
        film.setId((long) filmId);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlUpdateFilm = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rate = ?, mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlUpdateFilm,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate() != null ? film.getRate() : null,
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());

        return film;
    }

    @Override
    public void makeLike(int idFilm, int idUser) {
        String sqlInsertLikes = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlInsertLikes, idFilm, idUser);
        log.info("User с id=" + idUser + " поставил лайк film с id=" + idFilm);
    }

    @Override
    public void deleteLike(int idFilm, int idUser) {
        String sqlDelete = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlDelete, idFilm, idUser);
        log.info("User с id=" + idUser + " удалил свой лайк film с id=" + idFilm);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) throws SQLException {
        String sqlPopular = "SELECT f.*, g.*, m.mpa_name " +
                "FROM films f " +
                "LEFT OUTER JOIN film_genres fg ON fg.film_id = f.film_id " +
                "LEFT OUTER JOIN genres g ON g.genre_id = fg.genre_id " +
                "LEFT OUTER JOIN mpa m ON m.mpa_id = f.mpa_id " +
                "WHERE f.film_id IN (SELECT film_id FROM likes GROUP BY film_id ORDER BY COUNT(*) DESC LIMIT ?)";
        Collection<Film> popularFilms = jdbcTemplate.query(sqlPopular, FilmDbStorage::makeFilm, count);
        if (popularFilms.isEmpty()) {
            popularFilms = getAllFilms();
        }
        return popularFilms;
    }

    @Override
    public List<Integer> getFilmLikes(Integer id) {
        String sqlFilmLikesById = "SELECT * FROM likes WHERE film_id = ?";
        return jdbcTemplate.query(sqlFilmLikesById, (rs, rowNum) -> rs.getInt("user_id"), id);
    }

    @Override
    public MPA checkMpa(Film film) {
        if (film == null) {
            throw new IllegalArgumentException("Объект film не был инициализирован");
        }
        Long mpaId = film.getMpa().getId();
        String sqlMpa = "SELECT * FROM mpa WHERE mpa_id = ?";
        List<MPA> mpaById = jdbcTemplate.query(sqlMpa, FilmDbStorage::makeMpa, mpaId);
        if (mpaById.isEmpty() || mpaById.get(0) == null || !mpaById.get(0).getId().equals(mpaId)) {
            throw new NotFoundException(String.format("Не найден MPA с id: %s", mpaId));
        }
        MPA mpa = mpaById.get(0);
        return mpa;
    }

    @Override
    public List<Genre> getGenres(int idFilm) {
        String sqlGenres = "SELECT g.* FROM film_genres fg JOIN genres g ON fg.genre_id = g.genre_id WHERE fg.film_id = ?";
        List<Genre> filmGenres = jdbcTemplate.query(sqlGenres, FilmDbStorage::makeGenre, idFilm);
        return filmGenres;
    }


    @Override
    public List<Genre> checkGenre(Film film) {
        List<Genre> filmGenres = new ArrayList<>();
        if (film.getGenres() != null) {
            Set<Integer> genreSet = film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .collect(Collectors.toSet());
            String sqlGenresId = "SELECT * FROM genres WHERE genre_id IN (" +
                    String.join(",", Collections.nCopies(genreSet.size(), "?")) + ")";
            List<Genre> genresById = jdbcTemplate.query(sqlGenresId, FilmDbStorage::makeGenre, genreSet.toArray());
            for (Integer id : genreSet) {
                boolean found = false;
                for (Genre genre : genresById) {
                    if (genre.getId().equals(id)) {
                        found = true;
                        filmGenres.add(genre);
                        break;
                    }
                }
                if (!found) {
                    throw new NotFoundException(String.format("Не найден Genre с id: %s", id));
                }
            }
        }
        return filmGenres;
    }


    @Override
    public void insertFilmGenres(Film film) {
        String sqlInsertFilmGenres = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlInsertFilmGenres, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public void deleteFilmGenres(Film film) {
        String sqlDeleteFilmGenres = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteFilmGenres, film.getId());
    }

    public static MPA makeMpa(ResultSet rs, int rowNum) throws SQLException {
        MPA mpa = MPA.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name")).build();
        return mpa;
    }

    public static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name")).build();
        return genre;
    }

    public static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        MPA rating = MPA.builder().id(rs.getLong("mpa_id")).name(rs.getString("mpa_name")).build();
        Film film = Film.builder().id((long) rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(rating).build();
        return film;
    }


}




