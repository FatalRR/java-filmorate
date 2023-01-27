package ru.yandex.practicum.filmorate.messages;

public enum ExceptionMessages {
    EMPTY_NAME("Название фильма не может быть пустым"),
    MAX_DESCRIPTION("Максимальная длина описания — 200 символов."),
    POSITIVE_DURATION("Продолжительность должны быть положительной"),
    INCORRECT_EMAIL("Некорректный email адрес"),
    EMPTY_LOGIN("login не может быть пустой"),
    INCORRECT_BIRTHDAY("Дата рождения не может быть в будущем"),
    LOGIN_WITHOUT_SPACE("login не должен содержать пробелы");

    private final String text;

    ExceptionMessages(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
