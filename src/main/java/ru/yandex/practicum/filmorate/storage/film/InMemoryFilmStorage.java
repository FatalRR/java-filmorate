package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    private int generateId() {
        return id++;
    }

    @Override
    public List<Film> getAll() {
        log.info(String.valueOf(LogMessages.COUNT), films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film save(Film film) {
        log.info(String.valueOf(LogMessages.TRY_ADD), film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info(String.valueOf(LogMessages.ADD));
        return film;
    }

    @Override
    public Film update(Film film) {
        log.info(String.valueOf(LogMessages.TRY_UPDATE), film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException(String.valueOf(LogMessages.MISSING));
        }
        films.replace(film.getId(), film);
        log.info(String.valueOf(LogMessages.UPDATE));
        return film;
    }

    @Override
    public Film getById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException(String.valueOf(LogMessages.MISSING));
        }
    }

    @Override
    public Film remove(Film film) {
        if (films.containsKey(id)) {
            return films.remove(id);
        } else {
            throw new NotFoundException(String.valueOf(LogMessages.MISSING));
        }
    }
}