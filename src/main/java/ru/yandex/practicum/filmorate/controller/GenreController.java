package ru.yandex.practicum.filmorate.controller;

/*import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Double;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Double> getAll() {
        return genreService.getAll();
    }

    @GetMapping(path = "/{id}")
    public Double getById(@PathVariable("id") int genreID) {
        return genreService.get(genreID);
    }

}*/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreDbStorage genreDbStorage;

    @GetMapping
    public List<Genre> getAll() {
        log.info("Received GET request: all genres");
        return genreDbStorage.getAll();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable("id") Integer id) {
        log.info("Received GET request: genre {}", id);
        return genreDbStorage.getGenreById(id);
    }

    @PostMapping
    public Genre addGenre(@RequestBody @Valid Genre genre) {
        log.info("Received POST request: new genre");
        return genreDbStorage.addGenre(genre);
    }
}