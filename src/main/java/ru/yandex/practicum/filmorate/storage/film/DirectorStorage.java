package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getAll();

    Director getById(Integer directorId);

    Director save(Director director);

    Director update(Director director);

    void removeById(Integer id);
}