package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {
    List<T> getAll();

    T save(T t);

    T update(T t);

    T getById(int id);

    T remove(T t);
    void clearAll();
}