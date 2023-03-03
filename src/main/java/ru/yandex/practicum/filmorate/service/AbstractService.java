package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public abstract class AbstractService<T extends Entity> {
    protected Storage<T> storage;

    public List<T> getAll() {
        return storage.getAll();
    }

    public T save(T t) {
        validate(t);
        return storage.save(t);
    }

    public T update(T t) {
        validate(t);
        return storage.update(t);
    }

    public T remove(T t) {
        return storage.remove(t);
    }

    public T getById(int id) {
        return storage.getById(id);
    }

    public abstract T validate(T t) throws ValidationException;
}