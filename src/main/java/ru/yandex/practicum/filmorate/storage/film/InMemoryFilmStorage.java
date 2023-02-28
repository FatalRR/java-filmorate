package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
    @Override
    public List<Film> getAll() {
        return super.getAll();
    }

    @Override
    public Film save(Film film) {
        return super.save(film);
    }

    @Override
    public Film update(Film film) {
        return super.update(film);
    }

    @Override
    public Film getById(int id) {
        return super.getById(id);
    }

    @Override
    public Film remove(Film film) {
        return super.remove(film);
    }
}