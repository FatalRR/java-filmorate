package ru.yandex.practicum.filmorate.storage.user;

public interface FriendStorage {
    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);
}