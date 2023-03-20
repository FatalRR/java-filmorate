package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User save(User user) {
        validate(user);
        return userStorage.save(user);
    }

    public User update(User user) {
        validate(user);
        if (Objects.equals(getById(user.getId()).getId(), user.getId())) {
            return userStorage.update(user);
        } else {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
    }

    public User getById(Integer id) {
        try {
            return userStorage.getById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
    }

    public void addFriend(Integer id, Integer friendId) {
        try {
            userStorage.addFriend(id, friendId);
        } catch (Exception e) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
    }

    public void removeFriend(Integer id, Integer friendId) {
        try {
            userStorage.removeFriend(id, friendId);
        } catch (Exception e) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
    }

    public List<User> getFriends(Integer id) {
        return userStorage.getFriends(id);
    }

    public List<User> corporateFriends(Integer id, Integer otherId) {
        return userStorage.getCorparateFriends(id, otherId);
    }

    public void validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info(ValidationExceptionMessages.LOGIN_TO_NAME.toString());
            user.setName(user.getLogin());
        }
    }
}