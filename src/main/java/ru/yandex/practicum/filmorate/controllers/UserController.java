package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    @GetMapping
    @Override
    public Collection<User> findAll() {
        return super.findAll();
    }
    @PostMapping
    @Override
    public User create(@Valid @RequestBody User user) throws javax.validation.ValidationException {
        return super.create(user);
    }

    @PutMapping
    @Override
    public User put(@Valid @RequestBody User user) throws javax.validation.ValidationException {
        return super.put(user);
    }

    public User validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info(ValidationExceptionMessages.LOGIN_TO_NAME.toString());
            user.setName(user.getLogin());
        }
        return user;
    }
}