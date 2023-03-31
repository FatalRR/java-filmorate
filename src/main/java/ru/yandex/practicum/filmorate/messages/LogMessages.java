package ru.yandex.practicum.filmorate.messages;

public enum LogMessages {
    ADD("Объект успешно добавлен: {}"),
    UPDATE("Объект успешно обновлен: {}"),
    GET("Объект получен по id: {}"),
    TRY_ADD("Попытка добавить: {}"),
    TRY_UPDATE("Попытка обновить: {}"),
    TRY_REMOVE("Попытка удалить: {}"),
    TRY_GET_POPULAR("Попытка получить популярный объект: {}"),
    TRY_GET_OBJECT("Попытка получить объект: {}"),
    TRY_REMOVE_LIKE("Попытка удалить поставленный лайк: film_id {}, user_id {}"),
    TRY_ADD_LIKE("Попытка поставить лайк: film_id {}, user_id {}"),
    TRY_ADD_FRIEND("Попытка добавить друга: {}"),
    TRY_REMOVE_FRIEND("Попытка удалить друга: {}"),
    TRY_GET_FRIENDS("Попытка получить список друзей: {}"),
    TRY_GET_CORPORATE_FRIENDS("Попытка получить общих друзей: {}"),
    TRY_GET_RECOMMENDATIONS("Попытка получить рекомендации по фильмам для пользователя с id: {}"),
    MISSING("Такого объекта не существует"),
    COUNT("Количество объектов: {}"),
    FRIEND_DONE("Пользователи с id: {} и {} стали друзьями"),
    FRIEND_CANCEL("Пользователи с id: {} и {} больше не друзья"),
    LIKE_DONE("Пользователь с id: {} поставил лайк фильму с id: {}"),
    LIKE_CANCEL("Пользователь с id: {} удалил лайк у фильма с id: {}"),
    LIST_OF_FRIENDS("Список общих друзей пользователей: {}"),
    TRY_GET_DIRECTOR_FILM("Попытка получение списка фильмов режиссера directorId={}, sort={}."),
    LIST_OF_RECOMMENDATIONS("Предоставлен список рекомендованых фильмов пользователю с id: {}"),
    TRY_DELETE("Попытка удалить: {}"),
    REVIEW_LIKE_DONE("Пользователь с id: {} поставил лайк отзыву с id: {}"),
    REVIEW_LIKE_CANCEL("Пользователь с id: {} удалил лайк у отзыва с id: {}"),
    REVIEW_DISLIKE_DONE("Пользователь с id: {} поставил дизлайк отзыву с id: {}"),
    REVIEW_DISLIKE_CANCEL("Пользователь с id: {} удалил дизлайк у отзыва с id: {}");

    private final String textLog;

    LogMessages(String textLog) {
        this.textLog = textLog;
    }

    @Override
    public String toString() {
        return textLog;
    }
}