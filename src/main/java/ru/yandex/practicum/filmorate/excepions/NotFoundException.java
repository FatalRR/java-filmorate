package ru.yandex.practicum.filmorate.excepions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s);
    }
}