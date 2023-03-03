package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.AbstractService;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService extends AbstractService<User> {
    @Autowired
    public UserService(Storage<User> storage) {
        super.storage = storage;
    }

    private boolean checkFriendToFriend(int userId, int friendId) {
        return userId == friendId;
    }

    @Override
    public User validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info(ValidationExceptionMessages.LOGIN_TO_NAME.toString());
            user.setName(user.getLogin());
        }
        return user;
    }

    public User addFriend(int userId, int friendId) {
        if (checkFriendToFriend(userId, friendId))
            throw new ValidationException(String.valueOf(ValidationExceptionMessages.FRIEND_TO_FRIEND));
        getById(userId);
        getById(friendId);
        storage.getById(userId).getFriends().add(friendId);
        storage.getById(friendId).getFriends().add(userId);
        log.info(String.valueOf(LogMessages.FRIEND_DONE), userId, friendId);
        return storage.getById(userId);
    }

    public User removeFriend(int userId, int friendId) {
        if (checkFriendToFriend(userId, friendId))
            throw new ValidationException(String.valueOf(ValidationExceptionMessages.FRIEND_TO_FRIEND));
        getById(userId);
        getById(friendId);
        if (!storage.getById(userId).getFriends().contains(friendId) && !storage.getById(friendId).getFriends().contains(userId))
            throw new ValidationException(String.valueOf(ValidationExceptionMessages.NOT_FRIENDS));
        storage.getById(userId).getFriends().remove(friendId);
        storage.getById(friendId).getFriends().remove(userId);
        log.info(String.valueOf(LogMessages.FRIEND_CANCEL), userId, friendId);
        return storage.getById(userId);
    }

    public List<User> getFriends(int userId) {
        getById(userId);
        return storage.getById(userId).getFriends().stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }

    public List<User> corporateFriends(int userId, int friendId) {
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);

        List<User> mutualFriends = user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(storage::getById)
                .collect(Collectors.toList());

        log.info(String.valueOf(LogMessages.LIST_OF_FRIENDS));
        return mutualFriends;
    }
}