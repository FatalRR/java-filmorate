package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

public interface ReviewStorage {

    public Review save(Review review);

    public Review getById(Integer id);
}
