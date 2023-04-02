package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.enums.EventTypes;
import ru.yandex.practicum.filmorate.enums.OperationTypes;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface FeedStorage {
    List<Event> getByUserId(Integer userId);

    Event addEvent(Integer userId, EventTypes eventTypes, OperationTypes operationTypes, Integer entityId);
}