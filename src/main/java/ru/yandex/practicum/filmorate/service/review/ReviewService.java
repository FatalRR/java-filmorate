package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.enums.EventTypes;
import ru.yandex.practicum.filmorate.enums.OperationTypes;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDbStorage storage;
    private final UserService userService;
    private final FilmService filmService;

    public Review save(Review review) {
        userService.getById(review.getUserId());
        filmService.getById(review.getFilmId());
        Review addReview = storage.save(review);
        userService.addEvent(addReview.getUserId(), EventTypes.REVIEW, OperationTypes.ADD, addReview.getReviewId());
        return addReview;
    }

    public Review update(Review review) {
        userService.getById(review.getUserId());
        filmService.getById(review.getFilmId());
        Review updatedReview = storage.update(review);
        userService.addEvent(updatedReview.getUserId(), EventTypes.REVIEW, OperationTypes.UPDATE, updatedReview.getReviewId());
        return updatedReview;
    }

    public void delete(Integer id) {
        Review review = getById(id);
        storage.delete(id);
        userService.addEvent(review.getUserId(), EventTypes.REVIEW, OperationTypes.REMOVE, review.getReviewId());
    }

    public Review getById(Integer id) {
        return storage.getById(id);
    }

    public List<Review> getFilmReviews(Optional<Integer> filmId, Integer count) {
        filmId.ifPresent(filmService::getById);
        return storage.getFilmReviews(filmId, count);
    }

    public void changeUseful(Integer reviewId, Boolean isUsefulIncreasing) {
        this.getById(reviewId);
        storage.changeUseful(reviewId, isUsefulIncreasing);
    }

    public void saveLike(Integer reviewId, Integer userId, Boolean isLike) {
        userService.getById(userId);
        this.getById(reviewId);
        storage.saveLike(reviewId, userId, isLike);
    }

    public void deleteLike(Integer reviewId, Integer userId, Boolean isLike) {
        userService.getById(userId);
        this.getById(reviewId);
        storage.deleteLike(reviewId, userId, isLike);
    }
}