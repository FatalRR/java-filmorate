package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.enums.EventTypes;
import ru.yandex.practicum.filmorate.enums.OperationTypes;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewDbStorage storage;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewDbStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public Review save(Review review) {
        Review addReview = storage.save(review);
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(addReview.getUserId())
                .eventType(EventTypes.REVIEW)
                .operation(OperationTypes.ADD)
                .entityId(addReview.getReviewId())
                .eventId(0)
                .build();
        userService.addEvent(event);
        return addReview;
    }

    public Review update(Review review) {
        Review updatedReview = storage.update(review);
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(updatedReview.getUserId())
                .eventType(EventTypes.REVIEW)
                .operation(OperationTypes.UPDATE)
                .entityId(updatedReview.getReviewId())
                .eventId(0)
                .build();
        userService.addEvent(event);
        return updatedReview;
    }

    public void delete(Integer id) {
        Review review = getById(id);
        storage.delete(id);
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(review.getUserId())
                .eventType(EventTypes.REVIEW)
                .operation(OperationTypes.REMOVE)
                .entityId(review.getReviewId())
                .eventId(0)
                .build();
        userService.addEvent(event);
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
