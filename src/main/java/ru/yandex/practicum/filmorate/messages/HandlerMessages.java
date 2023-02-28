package ru.yandex.practicum.filmorate.messages;

public enum HandlerMessages {
    ERROR_400("Ошибка 400: {}"),
    ERROR_404("Ошибка 404: {}"),
    ERROR_500("Ошибка 500: {}"),
    VALID("Ошибка валидации"),
    NOT_FOUND("Не найден объект"),
    SERVER_ERROR("Ошибка сервера");

    private final String text;

    HandlerMessages(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}