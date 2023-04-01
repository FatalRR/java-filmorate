package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventTypes;
import ru.yandex.practicum.filmorate.enums.OperationTypes;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.FeedStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;
    private final FilmStorage filmStorage;
    private final FeedStorage feedStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(UserStorage userStorage,
                       FriendStorage friendStorage,
                       FilmStorage filmStorage,
                       FeedStorage feedStorage,
                       JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
        this.filmStorage = filmStorage;
        this.feedStorage = feedStorage;
        this.jdbcTemplate = jdbcTemplate;
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
            friendStorage.addFriend(id, friendId);
        } catch (Exception e) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(id)
                .eventType(EventTypes.FRIEND)
                .operation(OperationTypes.ADD)
                .entityId(friendId)
                .eventId(0)
                .build();
        addEvent(event);
    }

    public void removeFriend(Integer id, Integer friendId) {
        try {
            friendStorage.removeFriend(id, friendId);
        } catch (Exception e) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(id)
                .eventType(EventTypes.FRIEND)
                .operation(OperationTypes.REMOVE)
                .entityId(friendId)
                .eventId(0)
                .build();
        addEvent(event);
    }

    public List<User> getFriends(Integer id) {
        return userStorage.getFriends(id);
    }

    public List<User> corporateFriends(Integer id, Integer otherId) {
        return userStorage.getCorporateFriends(id, otherId);
    }

    public void validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info(ValidationExceptionMessages.LOGIN_TO_NAME.toString());
            user.setName(user.getLogin());
        }
    }

    public List<Film> recommendations(Integer userId) {
        String sqlRequst = " SELECT user_id FROM film_likes ";
        List<Integer> userIds = jdbcTemplate.queryForList(sqlRequst, Integer.class);

        Integer idMaxNumFilms = 0;
        int maxNumFilms = 0;

        for (Integer usrId : userIds) {
            if (!userId.equals(usrId)) {
                if (filmStorage.commonFilms(userId, usrId).size() > maxNumFilms) {
                    maxNumFilms = filmStorage.commonFilms(userId, usrId).size();
                    idMaxNumFilms = usrId;
                }
            }
        }

        List<Film> films = new ArrayList<>();
        filmStorage.differentFilms(userId, idMaxNumFilms).forEach(id -> films.add(filmStorage.getById(id)));
        log.info(String.valueOf(LogMessages.LIST_OF_RECOMMENDATIONS), userId);

        return films;
    }

    public List<Event> getByUserId(Integer userId) {
        return feedStorage.getByUserId(userId);
    }

    public Event addEvent(Event event) {
        return feedStorage.addEvent(event);
    }
}