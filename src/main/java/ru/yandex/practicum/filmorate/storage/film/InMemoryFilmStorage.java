package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private long nextId = 0;

    @Override
    public Long getNextFreeId() {
        return ++nextId;
    }

    @Override
    public Film createFilm(Film film) {
        long id = getNextFreeId();
        film.setId(id);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        if (!films.containsKey(film.getId())) {
            throw new DataNotFoundException("Ключ не найден: " + film.getId());
        } else {
            films.put(film.getId(), film);
            log.info("Запрос на изменение фильма. Фильм изменён.");
        }

        return film;
    }


    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id))
            throw new DataNotFoundException(String.format("Request film by id when id is absent, id = %d", id));
        return films.get(id);
    }


}
