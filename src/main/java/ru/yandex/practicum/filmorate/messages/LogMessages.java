package ru.yandex.practicum.filmorate.messages;

public enum LogMessages {
    ADD("Объект успешно добавлен: {}"),
    UPDATE("Объект успешно обновлен: {}"),
    TRY_ADD("Попытка добавить: {}"),
    TRY_UPDATE("Попытка обновить: {}"),
    MISSING("Такого объекта не существует"),
    COUNT("Количество объектов: {}"),
    FRIEND_DONE("Пользователи с id: {} и {} стали друзьями"),
    FRIEND_CANCEL("Пользователи с id: {} и {} больше не друзья"),
    LIKE_DONE("Пользователь с id: {} поставил лайк фильму с id: {}"),
    LIKE_CANCEL("Пользователь с id: {} удалил лайк у фильма с id: {}"),
    LIST_OF_FRIENDS("Список общих друзей пользователей");

    private final String textLog;

    LogMessages(String textLog) {
        this.textLog = textLog;
    }

    @Override
    public String toString() {
        return textLog;
    }
}