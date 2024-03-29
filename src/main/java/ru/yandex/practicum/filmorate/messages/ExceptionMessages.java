package ru.yandex.practicum.filmorate.messages;

public enum ExceptionMessages {
    ;
    public static final String EMPTY_NAME = "Название фильма не может быть пустым";
    public static final String MAX_DESCRIPTION = "Максимальная длина описания — 200 символов.";
    public static final String POSITIVE_DURATION = "Продолжительность должны быть положительной";
    public static final String INCORRECT_EMAIL = "Некорректный email адрес";
    public static final String INCORRECT_PAR = "Некорректные параметры поиска";
    public static final String EMPTY_LOGIN = "login не может быть пустой";
    public static final String INCORRECT_BIRTHDAY = "Дата рождения не может быть в будущем";
    public static final String LOGIN_WITHOUT_SPACE = "login не должен содержать пробелы";
    public static final String POSITIVE_ID = "id должен быть больше 0";
    public static final String NOT_GENRE = "Жанр не найден";
    public static final String NOT_MPA = "Рейтинг не найден";
    public static final String NOT_FOUND_ID = "Не найден объект по ID";
    public static final String NOT_OBJECT = "Не найден объект";
    public static final String NOT_DIRECTOR = "Не найден режиссер";
}