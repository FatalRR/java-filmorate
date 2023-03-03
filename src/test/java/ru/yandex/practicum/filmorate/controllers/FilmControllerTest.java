package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    FilmService filmService;
    @Autowired
    Storage<Film> storage;

    @AfterEach
    public void clearUp() {
        storage.clearAll();
    }

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
        System.out.println(filmService.getAll());
        filmService.save(film1);
        System.out.println(filmService.getAll());
        filmService.save(film2);
        System.out.println(filmService.getAll());
        assertEquals(2, filmService.getAll().size());
    }

    @Test
    void shouldCreateFilmWithIncorrectDate() {
        Film film = Film.builder()
                .name("Имя фильма 3")
                .description("Описание фильма 3")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(100)
                .build();
        ValidationException ex = assertThrows(ValidationException.class, () -> filmService.save(film));
        assertEquals("Дата релиза не может быть раньше чем 28.12.1895", ex.getMessage());
    }

    @Test
    void shouldUpdateFilm() {
        Film film1 = Film.builder()
                .name("Имя фильма 4")
                .description("Описание фильма 4")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .name("Имя фильма 5")
                .description("Описание фильма 5")
                .releaseDate(LocalDate.now())
                .duration(200)
                .build();

        filmService.save(film1);
        film2.setId(film1.getId());
        filmService.update(film2);
        Collection<Film> actualFilms = filmService.getAll();
        assertEquals(1, actualFilms.size());
        assertEquals(film2, actualFilms.iterator().next());
    }

    @Test
    void shouldUpdateFilmWithIncorrectId() {
        Film film1 = Film.builder()
                .name("Имя фильма 6")
                .description("Описание фильма 6")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .name("Имя фильма 7")
                .description("Описание фильма 7")
                .releaseDate(LocalDate.now())
                .duration(200)
                .build();
        filmService.save(film1);
        film2.setId(99);
        ru.yandex.practicum.filmorate.excepions.NotFoundException ex = assertThrows(ru.yandex.practicum.filmorate.excepions.NotFoundException.class, () -> filmService.update(film2));
        assertEquals("Такого объекта не существует", ex.getMessage());
    }
}