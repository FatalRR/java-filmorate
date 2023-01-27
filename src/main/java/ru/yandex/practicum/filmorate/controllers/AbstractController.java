package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Entity;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractController<T extends Entity> {
    private int id = 1;
    private final Map<Integer, T> itemsMap = new HashMap<>();

    private int generatorId() {
        return id++;
    }

    public Collection<T> findAll() {
        log.info(String.valueOf(LogMessages.COUNT), itemsMap.size());
        return Collections.unmodifiableCollection(itemsMap.values());
    }


    public T create(T t) throws ValidationException {
        log.debug(String.valueOf(LogMessages.TRY_ADD), t);
        validate(t);

        t.setId(generatorId());
        itemsMap.put(t.getId(),t);
        log.debug(String.valueOf(LogMessages.ADD), t);
        return t;
    }

    public T put(T t) throws ValidationException {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), t);
        validate(t);

        if (itemsMap.get(t.getId()) != null) {
            itemsMap.replace(t.getId(), t);
            log.debug(String.valueOf(LogMessages.UPDATE), t);
        } else {
            log.debug(String.valueOf(LogMessages.MISSING));
            throw new ValidationException(String.valueOf(LogMessages.MISSING));
        }
        return t;
    }

    public abstract T validate(T t) throws ValidationException;
}