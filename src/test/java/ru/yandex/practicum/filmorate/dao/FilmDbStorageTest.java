package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;

    @Test
    void shouldSave() {
        Mpa mpa = Mpa.builder().id(1).build();
        Genre genre = Genre.builder().id(1).build();
        Film film = Film.builder()
                .name("тестовый фильм 1")
                .description("описание тестового фильма 1")
                .releaseDate(LocalDate.of(1988, 11, 8))
                .duration(107)
                .mpa(mpa)
                .build();
        film.addFilmGenre(genre);

        Film saveFilm = filmDbStorage.save(film);
        assertEquals(saveFilm.getName(), film.getName());
        assertEquals("G", saveFilm.getMpa().getName());
        Assertions.assertTrue(saveFilm.getGenres().contains(genre));
    }

    @Test
    void shouldUpdate() {
        Mpa mpa = Mpa.builder().id(1).build();
        Genre genre = Genre.builder().id(1).build();
        Film film = Film.builder()
                .name("тестовый фильм 2")
                .description("описание тестового фильма 2")
                .releaseDate(LocalDate.of(1988, 11, 8))
                .duration(120)
                .mpa(mpa)
                .build();
        film.addFilmGenre(genre);

        Film updateFilm = filmDbStorage.save(film);
        Mpa updateRate = Mpa.builder().id(2).build();
        Genre updateGenre = Genre.builder().id(3).build();
        updateFilm.setName("новый тестовый фильм 2");
        updateFilm.setDescription("новое описание тестового фильма 2");
        updateFilm.setReleaseDate(LocalDate.of(1993, 6, 14));
        updateFilm.setDuration(102);
        updateFilm.setMpa(updateRate);
        updateFilm.addFilmGenre(updateGenre);

        Film updatedFilm = filmDbStorage.update(updateFilm);

        assertEquals(updatedFilm.getName(), updateFilm.getName());
        assertEquals("PG", updatedFilm.getMpa().getName());
        Assertions.assertTrue(updatedFilm.getGenres().contains(updateGenre));
    }

    @Test
    void shouldGetById() {
        Mpa mpa = Mpa.builder().id(1).build();
        Genre genre = Genre.builder().id(1).build();
        Film film = Film.builder()
                .name("тестовый фильм 3")
                .description("описание тестового фильма 3")
                .releaseDate(LocalDate.of(1950, 4, 13))
                .duration(30)
                .mpa(mpa)
                .build();
        film.addFilmGenre(genre);

        Film saveFilm = filmDbStorage.save(film);
        Film getFilm = filmDbStorage.getById(saveFilm.getReviewId());

        assertEquals(getFilm.getName(), film.getName());
        assertEquals("G", getFilm.getMpa().getName());
        Assertions.assertTrue(getFilm.getGenres().contains(genre));
    }

    @Test
    void shouldAddLikeAndGetPopularFilm() {
        User user = User.builder()
                .email("FatalR@yandex.ru")
                .login("FatalR")
                .name("Sergey")
                .birthday(LocalDate.of(1993, 6, 14))
                .build();

        Mpa mpa = Mpa.builder().id(3).build();
        Genre genre = Genre.builder().id(6).build();
        Film film = Film.builder()
                .name("тестовый фильм 5")
                .description("описание тестового фильма 5")
                .releaseDate(LocalDate.of(2022, 12, 31))
                .duration(120)
                .mpa(mpa)
                .build();

        film.addFilmGenre(genre);
        Film saveFilm = filmDbStorage.save(film);
        User likeUser = userDbStorage.save(user);
        likeDbStorage.addLike(saveFilm.getReviewId(), likeUser.getReviewId());

        List<Film> listFilm = filmDbStorage.getPopular(1);
        assertEquals(listFilm.get(0).getName(), saveFilm.getName());
    }

    @Test
    void shouldGetAll() {
        Mpa mpa = Mpa.builder().id(3).build();
        Genre genre = Genre.builder().id(4).build();
        Film film = Film.builder()
                .name("тестовый фильм 3")
                .description("описание тестового фильма 3")
                .releaseDate(LocalDate.of(1950, 4, 13))
                .duration(30)
                .mpa(mpa)
                .build();
        film.addFilmGenre(genre);
        filmDbStorage.save(film);

        List<Film> getAll = filmDbStorage.getAll();
        assertEquals(1, getAll.size());
    }
}