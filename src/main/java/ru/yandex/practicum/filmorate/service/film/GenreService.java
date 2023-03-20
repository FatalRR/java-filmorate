package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre getById(Integer genreId) {
        try {
            log.info(String.valueOf(LogMessages.GET), genreId);
            return genreStorage.getById(genreId);
        } catch (Exception e) {
            throw new NotFoundException(ExceptionMessages.NOT_GENRE);
        }
    }
}