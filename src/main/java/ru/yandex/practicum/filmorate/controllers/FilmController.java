package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.enums.FilmSort;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
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
    public Film getById(@PathVariable Integer filmId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), filmId);
        return filmService.getById(filmId);
    }

    @DeleteMapping("/{filmId}")
    public void removeById(@PathVariable Integer filmId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_OBJECT), filmId);
        filmService.removeById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Integer addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD_LIKE), filmId, userId);
        filmService.addLike(filmId, userId);
        return filmId;
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Integer removeLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_LIKE), filmId, userId);
        filmService.removeLike(filmId, userId);
        return filmId;
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Integer count, @RequestParam (required = false) Integer genreId, @RequestParam(required = false) Integer year) {
        log.debug(String.valueOf(LogMessages.TRY_GET_POPULAR));
        return filmService.getPopular(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    public  List<Film> getDirectorFilm(@PathVariable Integer directorId, @RequestParam FilmSort sortBy) {
        log.debug(String.valueOf(LogMessages.TRY_GET_DIRECTOR_FILM), directorId, sortBy);
        return filmService.getDirectorFilm(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam String query, @RequestParam String by) {
        log.debug(String.valueOf(LogMessages.TRY_GET_SEARCH), query, by);
        return filmService.getSearch(query, by);
    }

    @GetMapping("/common")
    public List<Film> commonFilms(@RequestParam("userId") Integer userId, @RequestParam("friendId") Integer friendId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_COMMON_FILMS), userId, friendId);
        return filmService.getCommon(userId, friendId);
    }

}
