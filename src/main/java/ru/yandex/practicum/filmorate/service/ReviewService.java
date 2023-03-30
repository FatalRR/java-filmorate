package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

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

    public Review update(Review review) {
        return storage.update(review);
    }

    public void delete(Integer id) {
        storage.delete(id);
    }

    public Review getById(Integer id) {
        return storage.getById(id);
    }

    public List<Review> getFilmReviews(Optional<Integer> filmId, Integer count) {
        return storage.getFilmReviews(filmId, count);
    }

    public void changeUseful(Integer reviewId, Boolean isUsefulIncreasing) {
        storage.changeUseful(reviewId, isUsefulIncreasing);
    }
}
