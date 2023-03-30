package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Review save(Review review);

    Review update(Review review);

    void delete(Integer id);

    Review getById(Integer id);

    List<Review> getFilmReviews(Optional<Integer> filmId, Integer count);

    List<Review> getAllReviews();

    void changeUseful(Integer reviewId, Boolean isUsefulIncreasing);
}
