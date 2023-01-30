package ru.yandex.practicum.filmorate.messages;

public enum LogMessages {
    ADD("Объект успешно добавлен: {}"),
    UPDATE("Объект успешно обновлен: {}"),
    TRY_ADD("Попытка добавить: {}"),
    TRY_UPDATE("Попытка обновить: {}"),
    MISSING("Такого объекта не существует"),
    COUNT("Количество объектов: {}");

    private final String textLog;

    LogMessages(String textLog) {
        this.textLog = textLog;
    }

    @Override
    public String toString() {
        return textLog;
    }
}