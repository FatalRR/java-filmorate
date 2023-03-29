package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.film.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@Slf4j
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> getAll() {
        log.info(String.valueOf(LogMessages.COUNT), directorService.getAll().size());
        return directorService.getAll();
    }

    @GetMapping("/{directorId}")
    public Director getById(@PathVariable Integer directorId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), directorId);
        return directorService.getById(directorId);
    }

    @PostMapping
    public Director save(@Valid @RequestBody Director director) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), director);
        return directorService.save(director);
    }

    @PutMapping
    public Director update (@Valid @RequestBody Director director) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), director);
        return directorService.update(director);
    }

    @DeleteMapping("/{directorId}")
    public void removeById (@PathVariable Integer directorId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE), directorId);
        directorService.removeById(directorId);
    }
}