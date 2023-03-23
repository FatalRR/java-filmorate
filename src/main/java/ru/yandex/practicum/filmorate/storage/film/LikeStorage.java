package ru.yandex.practicum.filmorate.storage.film;

public interface LikeStorage {
    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);
}