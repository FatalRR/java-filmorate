package ru.yandex.practicum.filmorate.excepions;


public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
