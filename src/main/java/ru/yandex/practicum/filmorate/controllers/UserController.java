package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        log.debug(String.valueOf(LogMessages.COUNT), userService.getAll().size());
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
    public User getUserById(@PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void removeById(@PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_OBJECT), userId);
        userService.removeById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public Integer addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD_FRIEND));
        userService.addFriend(userId, friendId);
        return friendId;
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Integer deleteFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_FRIEND));
        userService.removeFriend(userId, friendId);
        return friendId;
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_FRIENDS));
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCorporateFriends(@PathVariable Integer userId, @PathVariable Integer otherId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_CORPORATE_FRIENDS));
        return userService.corporateFriends(userId, otherId);
    }

    @GetMapping("/{userId}/recommendations")
    public List<Film> recommendations(@PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_RECOMMENDATIONS), userId);
        return userService.recommendations(userId);
    }
}