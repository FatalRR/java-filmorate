package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public abstract class InMemoryStorage<T extends Entity> {
    private final Map<Integer, T> storage = new HashMap<>();
    private int id = 1;

    public int generateId() {
        return id++;
    }

    private boolean checkContainsKeys(int id) {
        return storage.containsKey(id);
    }

    public List<T> getAll() {
        log.info(String.valueOf(LogMessages.COUNT), storage.size());
        return new ArrayList<>(storage.values());
    }

    public T save(T item) {
        log.info(String.valueOf(LogMessages.TRY_ADD), item);
        item.setId(generateId());
        storage.put(item.getId(), item);
        log.info(String.valueOf(LogMessages.ADD));
        return item;
    }

    public T update(T item) {
        log.info(String.valueOf(LogMessages.TRY_UPDATE), item);
        if (!checkContainsKeys(item.getId())) {
            throw new NotFoundException(String.valueOf(LogMessages.MISSING));
        }
        storage.replace(item.getId(), item);
        log.info(String.valueOf(LogMessages.UPDATE));
        return item;
    }

    public T getById(int id) {
        if (checkContainsKeys(id)) {
            return storage.get(id);
        } else {
            throw new NotFoundException(String.valueOf(LogMessages.MISSING));
        }
    }

    public T remove(T t) {
        if (checkContainsKeys(t.getId())) {
            return storage.remove(t.getId());
        } else {
            throw new NotFoundException(String.valueOf(LogMessages.MISSING));
        }
    }

    public void clearAll() {
        storage.clear();
    }
}