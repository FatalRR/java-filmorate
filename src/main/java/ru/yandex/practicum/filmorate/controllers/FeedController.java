package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FeedController {
    private final UserService userService;

    @GetMapping("/users/{id}/feed")
    public List<Event> findById(@PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_EVENT_FEED), userId);
        userService.getById(userId);
        return userService.getByUserId(userId);
    }
}
