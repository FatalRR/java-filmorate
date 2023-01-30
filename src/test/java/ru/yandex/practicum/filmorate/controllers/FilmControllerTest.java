package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest  extends FilmController {
    FilmController filmController = new FilmController();
    @Test
    void shouldGetAllFilms() {
        Film film1 = Film.builder()
                .name("Имя фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .name("Имя фильма 2")
                .description("Описание фильма 2")
                .releaseDate(LocalDate.now())
                .duration(200)
                .build();
        filmController.create(film1);
        filmController.create(film2);
        assertEquals(2,filmController.findAll().size());
    }

    @Test
    void shouldCreateFilmWithIncorrectDate() {
        Film film = Film.builder()
                .name("Имя фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(1800,1,1))
                .duration(100)
                .build();
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Дата релиза не может быть раньше чем 28.12.1895", ex.getMessage());
    }

    @Test
    void shouldUpdateFilm() {
        Film film1 = Film.builder()
                .name("Имя фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .name("Имя фильма 2")
                .description("Описание фильма 2")
                .releaseDate(LocalDate.now())
                .duration(200)
                .build();

        filmController.create(film1);
        film2.setId(film1.getId());
        filmController.put(film2);
        Collection<Film> actualFilms = filmController.findAll();
        assertEquals(1, actualFilms.size());
        assertEquals(film2, actualFilms.iterator().next());
    }

    @Test
    void shouldUpdateFilmWithIncorrectId(){
        Film film1 = Film.builder()
                .name("Имя фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .name("Имя фильма 2")
                .description("Описание фильма 2")
                .releaseDate(LocalDate.now())
                .duration(200)
                .build();
        filmController.create(film1);
        film2.setId(99);
        javax.validation.ValidationException ex = assertThrows(javax.validation.ValidationException.class, () -> filmController.put(film2));
        assertEquals("Такого объекта не существует", ex.getMessage());
    }
}