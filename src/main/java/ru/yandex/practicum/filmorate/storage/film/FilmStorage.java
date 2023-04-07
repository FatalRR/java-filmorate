package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.enums.FilmSort;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film save(Film film);

    Film update(Film film);

    Film getById(Integer id);

    void removeById(Integer id);

    List<Film> getPopular(Integer count, Integer genreId, Integer year);

    List<Film> getDirectorFilm(Integer directorId, FilmSort sortBy);

    List<Film> addFilm(String sqlQuery);

    List<Film> getCommon(Integer userId, Integer friendId);

    List<Film> recommendations(Integer userId);
}