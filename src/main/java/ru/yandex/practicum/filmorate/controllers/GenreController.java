package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAll() {
        log.info(String.valueOf(LogMessages.COUNT), genreService.getAll().size());
        return genreService.getAll();
    }

    @GetMapping("/{genreId}")
    public Genre getById(@PathVariable Integer genreId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), genreId);
        return genreService.getById(genreId);
    }
}