package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventTypes;
import ru.yandex.practicum.filmorate.enums.OperationTypes;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSort;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.SearchStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final SearchStorage searchStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       LikeStorage likeStorage,
                       SearchStorage searchStorage,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.searchStorage = searchStorage;
        this.userService = userService;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film save(Film film) {
        validate(film);
        log.info(String.valueOf(LogMessages.ADD), film);
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        validate(film);
        log.info(String.valueOf(LogMessages.UPDATE), film);
        return filmStorage.update(film);
    }

    public Film getById(Integer id) {
        log.info(String.valueOf(LogMessages.GET), id);
        return filmStorage.getById(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        likeStorage.addLike(filmId, userId);
        log.info(String.valueOf(LogMessages.LIKE_DONE), userId, filmId);
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(userId)
                .eventType(EventTypes.LIKE)
                .operation(OperationTypes.ADD)
                .entityId(filmId)
                .eventId(0)
                .build();
        userService.addEvent(event);
    }

    public void removeLike(Integer filmId, Integer userId) {
        if (filmId >= 1 && userId >= 1) {
            likeStorage.removeLike(filmId, userId);
            log.info(String.valueOf(LogMessages.LIKE_CANCEL), userId, filmId);
        } else {
            throw new NotFoundException(ExceptionMessages.POSITIVE_ID);
        }
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(userId)
                .eventType(EventTypes.LIKE)
                .operation(OperationTypes.REMOVE)
                .entityId(filmId)
                .eventId(0)
                .build();
        userService.addEvent(event);
    }

    public List<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }

    public List<Film> getDirectorFilm(Integer directorId, FilmSort sortBy) {
        return filmStorage.getDirectorFilm(directorId, sortBy);
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            throw new ValidationException(ValidationExceptionMessages.RELEASE_DATE.toString());
        }
    }

    public List<Film> getSearch(String query, String by) {
        return searchStorage.getSearch(query, by);
    }

}