package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getAll() {
        log.info(String.valueOf(LogMessages.COUNT), mpaService.getAll().size());
        return mpaService.getAll();
    }

    @GetMapping("/{mpaId}")
    public Mpa getById(@PathVariable Integer mpaId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), mpaId);
        return mpaService.getById(mpaId);
    }
}