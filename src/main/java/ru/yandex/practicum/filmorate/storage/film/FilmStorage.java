package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film save(Film film);

    Film update(Film film);

    Film getById(Integer id);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> getPopular(Integer count);
}