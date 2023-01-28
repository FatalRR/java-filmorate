package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {
    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping
    @Override
    public Collection<Film> findAll() {
        return super.findAll();
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        return super.create(film);
    }

    @PutMapping
    @Override
    public Film put(@Valid @RequestBody Film film) throws ValidationException {
        return super.put(film);
    }

    public Film validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            throw new ValidationException(ValidationExceptionMessages.RELEASE_DATE.toString());
        }
        return film;
    }
}