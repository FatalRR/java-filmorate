package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.review.ReviewService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final FilmService filmService;

    @PostMapping
    public Review save(@Valid @RequestBody Review review) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), review);
        userService.getById(review.getUserId());
        filmService.getById(review.getFilmId());
        return reviewService.save(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), review);
        userService.getById(review.getUserId());
        filmService.getById(review.getFilmId());
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        log.debug(String.valueOf(LogMessages.TRY_DELETE), id);
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable Integer id) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), id);
        return reviewService.getById(id);
    }

    @GetMapping
    public List<Review> getFilmReviews(@RequestParam Optional<Integer> filmId,
                                       @RequestParam(defaultValue = "10") Integer count) {
        filmId.ifPresent(filmService::getById);
        log.debug(String.valueOf(LogMessages.COUNT), reviewService.getFilmReviews(filmId, count).size());
        return reviewService.getFilmReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), "лайк");
        userService.getById(userId);
        reviewService.getById(id);
        reviewService.saveLike(id, userId, true);
        reviewService.changeUseful(id, true);
        log.debug(String.valueOf(LogMessages.REVIEW_LIKE_DONE), userId, id);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), "дизлайк");
        userService.getById(userId);
        reviewService.getById(id);
        reviewService.saveLike(id, userId, false);
        reviewService.changeUseful(id, false);
        log.debug(String.valueOf(LogMessages.REVIEW_DISLIKE_DONE), userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_DELETE), "лайк");
        userService.getById(userId);
        reviewService.deleteLike(id, userId, true);
        reviewService.changeUseful(id, false);
        log.debug(String.valueOf(LogMessages.REVIEW_LIKE_CANCEL), userId, id);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_DELETE), "дизлайк");
        userService.getById(userId);
        userService.getById(userId);
        reviewService.deleteLike(id, userId, false);
        reviewService.changeUseful(id, true);
        log.debug(String.valueOf(LogMessages.REVIEW_DISLIKE_CANCEL), userId, id);
    }
}