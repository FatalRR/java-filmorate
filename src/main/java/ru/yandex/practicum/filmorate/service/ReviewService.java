package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.model.Review;

@Service
public class ReviewService {

    private final ReviewDbStorage storage;

    @Autowired
    public ReviewService(ReviewDbStorage storage) {
        this.storage = storage;
    }

    public Review save(Review review) {
        return storage.save(review);
    }

    public Review getById(Integer id) {
        try {
            return storage.getById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
    }
}
