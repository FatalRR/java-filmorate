package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedController {
    private final UserService userService;

    @GetMapping("/users/{id}/feed")
    public List<Event> findById(@PathVariable Integer id) {
        userService.getById(id);
        return userService.getByUserId(id);
    }
}
