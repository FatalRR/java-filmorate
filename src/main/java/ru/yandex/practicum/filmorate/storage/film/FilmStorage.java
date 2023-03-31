package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSort;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film save(Film film);

    Film update(Film film);

    Film getById(Integer id);

    List<Film> getPopular(Integer count);

    List<Film> getDirectorFilm(Integer directorId, FilmSort sortBy);

    List<Film> addFilm(String sqlQuery);

    List<Integer> differentFilms(final Integer mainUserId, final Integer otherUserId);

    List<Integer> commonFilms(final Integer userId, final Integer otherUserId);
}