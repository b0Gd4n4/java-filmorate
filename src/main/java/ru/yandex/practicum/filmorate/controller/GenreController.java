package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    private Collection<Genre> getAllGenres() {
        log.info("Запрос на получение всех жанров.");
        return genreService.getAllGenres();
    }

    @GetMapping(value = "/{id}")
    private Genre getGenreById(@PathVariable int id) {
        log.info("Запрос на получения жанра {} по id", id);
        return genreService.getGenreById(id);
    }
}