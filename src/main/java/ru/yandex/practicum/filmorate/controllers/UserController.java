package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends CommonController<User> {

    public User validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Установлен логин вместо имени пользователя");
            user.setName(user.getLogin());
        }
        return user;
    }
}