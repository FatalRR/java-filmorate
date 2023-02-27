package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 28);

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film save(Film film) {
        validate(film);
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public Film remove(Film film) {
        return filmStorage.remove(film);
    }

    public Film addLike(int filmId, int userId) {
        getById(filmId);
        filmStorage.getById(filmId).getLikes().add(userId);
        log.info(String.valueOf(LogMessages.LIKE_DONE), userId, filmId);
        return filmStorage.getById(filmId);
    }

    public Film removeLike(int filmId, int userId) {
        getById(filmId);
        if (!filmStorage.getById(filmId).getLikes().contains(userId))
            throw new NotFoundException(String.valueOf(ValidationExceptionMessages.WITHOUT_LIKE));
        filmStorage.getById(filmId).getLikes().remove(userId);
        log.info(String.valueOf(LogMessages.LIKE_CANCEL), userId, filmId);
        return filmStorage.getById(filmId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film validate(Film film) {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            throw new ValidationException(ValidationExceptionMessages.RELEASE_DATE.toString());
        }
        return film;
    }
}