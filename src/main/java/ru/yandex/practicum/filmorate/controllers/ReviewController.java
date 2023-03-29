package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {

    private final ReviewService service;

    @Autowired
    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @PostMapping
    public Review save(@Valid @RequestBody Review review) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), review);
        return service.save(review);
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable Integer id) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), id);
        return service.getById(id);
    }
}
