package ru.yandex.practicum.filmorate.messages;

public enum ValidationExceptionMessages {
    LOGIN_TO_NAME("Установлен логин вместо имени пользователя"),
    RELEASE_DATE("Дата релиза не может быть раньше чем 28.12.1895");

    private final String text;

    ValidationExceptionMessages(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}