package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User save(User user);

    User update(User user);

    User getById(Integer id);

    List<User> getFriends(Integer id);

    List<User> getCorporateFriends(Integer id, Integer otherId);
}