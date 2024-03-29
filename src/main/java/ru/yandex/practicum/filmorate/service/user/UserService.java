package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventTypes;
import ru.yandex.practicum.filmorate.enums.OperationTypes;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.FeedStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;
    private final FilmStorage filmStorage;
    private final FeedStorage feedStorage;
    private final JdbcTemplate jdbcTemplate;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User update(User user) {
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

    public void removeById(Integer id) {
        try {
            userStorage.removeById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
    }

    public void addFriend(Integer id, Integer friendId) {
        try {
            friendStorage.addFriend(id, friendId);
        } catch (Exception e) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
        addEvent(id, EventTypes.FRIEND, OperationTypes.ADD, friendId);
    }

    public void removeFriend(Integer id, Integer friendId) {
        try {
            friendStorage.removeFriend(id, friendId);
        } catch (Exception e) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
        addEvent(id, EventTypes.FRIEND, OperationTypes.REMOVE, friendId);
    }

    public List<User> getFriends(Integer id) {
        return userStorage.getFriends(id);
    }

    public List<User> corporateFriends(Integer id, Integer otherId) {
        return userStorage.getCorporateFriends(id, otherId);
    }

    public List<Film> recommendations(Integer userId) {
        log.info(String.valueOf(LogMessages.LIST_OF_RECOMMENDATIONS), userId);
        return filmStorage.recommendations(userId);
    }

    public List<Event> getByUserId(Integer userId) {
        return feedStorage.getByUserId(userId);
    }

    public Event addEvent(Integer userId, EventTypes eventTypes, OperationTypes operationTypes, Integer entityId) {
        return feedStorage.addEvent(userId, eventTypes, operationTypes, entityId);
    }

}