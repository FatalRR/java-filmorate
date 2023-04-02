package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.enums.EventTypes;
import ru.yandex.practicum.filmorate.enums.OperationTypes;

@Data
@AllArgsConstructor
@SuperBuilder
public class Event {
    private Long timestamp;
    private Integer userId;
    private EventTypes eventType;
    private OperationTypes operation;
    private Integer eventId;
    private Integer entityId;
}