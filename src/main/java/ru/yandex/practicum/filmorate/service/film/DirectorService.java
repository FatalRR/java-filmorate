package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public Director getById(Integer genreId) {
        try {
            log.info(String.valueOf(LogMessages.GET), genreId);
            return directorStorage.getById(genreId);
        } catch (Exception e) {
            throw new NotFoundException(ExceptionMessages.NOT_GENRE);
        }
    }

    public Director save(Director director) {
        return directorStorage.save(director);
    }

    public Director update(Director director) {
        return directorStorage.update(director);
    }

    public void removeById(Integer id) {
        directorStorage.removeById(id);
    }
}