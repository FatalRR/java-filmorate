package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        log.debug(String.valueOf(LogMessages.COUNT), filmService.getAll().size());
        return filmService.getAll();
    }

    @PostMapping
    public Film save(@Valid @RequestBody Film film) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), film);
        return filmService.save(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), film);
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film getById(@PathVariable int filmId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT));
        return filmService.getById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable int filmId, @PathVariable int userId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD_LIKE));
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film removeLike(@PathVariable int filmId, @PathVariable int userId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_LIKE));
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.debug(String.valueOf(LogMessages.TRY_GET_POPULAR));
        return filmService.getPopular(count);
    }
}