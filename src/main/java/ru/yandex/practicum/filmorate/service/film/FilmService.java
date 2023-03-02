package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.AbstractService;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService extends AbstractService<Film> {
    @Autowired
    public FilmService(Storage<Film> storage) {
        super.storage = storage;
    }

    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 28);
    private static final Comparator<Film> LIKES_COMPARATOR = (o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size());

    @Override
    public Film validate(Film film) {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            throw new ValidationException(ValidationExceptionMessages.RELEASE_DATE.toString());
        }
        return film;
    }

    public Film addLike(int filmId, int userId) {
        getById(filmId);
        storage.getById(filmId).getLikes().add(userId);
        log.info(String.valueOf(LogMessages.LIKE_DONE), userId, filmId);
        return storage.getById(filmId);
    }

    public Film removeLike(int filmId, int userId) {
        getById(filmId);
        if (!storage.getById(filmId).getLikes().contains(userId))
            throw new NotFoundException(String.valueOf(ValidationExceptionMessages.WITHOUT_LIKE));
        storage.getById(filmId).getLikes().remove(userId);
        log.info(String.valueOf(LogMessages.LIKE_CANCEL), userId, filmId);
        return storage.getById(filmId);
    }

    public List<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted(LIKES_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }
}