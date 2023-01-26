package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@RestController
@RequestMapping("/films")
public class FilmController extends CommonController<Film> {
    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895,12,28);

    public Film validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше чем 28.12.1895");
        }
        return film;
    }
}