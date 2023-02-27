package ru.yandex.practicum.filmorate.controllers;

import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.excepions.ValidationException;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorsHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidationException(final ValidationException e) {
        log.info("Ошибка 400: {}", e.getMessage());
        return Map.of("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlerNotFoundException(final NotFoundException e) {
        log.info("Ошибка 404: {}", e.getMessage());
        return Map.of("Не найден объект", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerInternalException(final ExecutionControl.InternalException e) {
        log.info("Ошибка 500: {}", e.getMessage());
        return Map.of("Ошибка сервера", e.getMessage());
    }
}