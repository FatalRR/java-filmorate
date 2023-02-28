package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private User validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info(ValidationExceptionMessages.LOGIN_TO_NAME.toString());
            user.setName(user.getLogin());
        }
        return user;
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
        return userStorage.update(user);
    }

    public User remove(User user) {
        return userStorage.remove(user);
    }

    public User getById(int id) {
        return userStorage.getById(id);
    }

    public User addFriend(int userId, int friendId) {
        if (userId == friendId)
            throw new ValidationException(String.valueOf(ValidationExceptionMessages.FRIEND_TO_FRIEND));
        getById(userId);
        getById(friendId);
        userStorage.getById(userId).getFriends().add(friendId);
        userStorage.getById(friendId).getFriends().add(userId);
        log.info(String.valueOf(LogMessages.FRIEND_DONE), userId, friendId);
        return userStorage.getById(userId);
    }

    public User removeFriend(int userId, int friendId) {
        if (userId == friendId)
            throw new ValidationException(String.valueOf(ValidationExceptionMessages.FRIEND_TO_FRIEND));
        getById(userId);
        getById(friendId);
        if (!userStorage.getById(userId).getFriends().contains(friendId) && !userStorage.getById(friendId).getFriends().contains(userId))
            throw new ValidationException(String.valueOf(ValidationExceptionMessages.NOT_FRIENDS));
        userStorage.getById(userId).getFriends().remove(friendId);
        userStorage.getById(friendId).getFriends().remove(userId);
        log.info(String.valueOf(LogMessages.FRIEND_CANCEL), userId, friendId);
        return userStorage.getById(userId);
    }

    public List<User> getFriends(int userId) {
        getById(userId);
        return userStorage.getById(userId).getFriends().stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public List<User> corporateFriends(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        List<User> mutualFriends = user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(userStorage::getById)
                .collect(Collectors.toList());

        log.info(String.valueOf(LogMessages.LIST_OF_FRIENDS));
        return mutualFriends;
    }
}