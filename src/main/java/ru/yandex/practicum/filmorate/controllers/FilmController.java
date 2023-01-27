package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {
    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895,12,28);

    public Film validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            throw new ValidationException(ValidationExceptionMessages.RELEASE_DATE.toString());
        }
        return film;
    }
}