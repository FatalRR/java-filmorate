package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        log.info(String.valueOf(LogMessages.COUNT), userService.getAll().size());
        return userService.getAll();
    }

    @PostMapping
    public User save(@Valid @RequestBody User user) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), user);
        return userService.save(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), user);
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT));
        return userService.getById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD_FRIEND));
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_FRIEND));
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable int userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_FRIENDS));
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCorporateFriends(@PathVariable int userId, @PathVariable int otherId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_CORPORATE_FRIENDS));
        return userService.corporateFriends(userId, otherId);
    }
}