package ru.yandex.practicum.filmorate.messages;

public enum ExceptionMessages {
    ;
    public static final String EMPTY_NAME = "Название фильма не может быть пустым";
    public static final String MAX_DESCRIPTION = "Максимальная длина описания — 200 символов.";
    public static final String POSITIVE_DURATION = "Продолжительность должны быть положительной";
    public static final String INCORRECT_EMAIL = "Некорректный email адрес";
    public static final String EMPTY_LOGIN = "login не может быть пустой";
    public static final String INCORRECT_BIRTHDAY = "Дата рождения не может быть в будущем";
    public static final String LOGIN_WITHOUT_SPACE = "login не должен содержать пробелы";
}