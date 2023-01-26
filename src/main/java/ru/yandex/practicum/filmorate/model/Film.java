package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class Film implements Item {
    private int id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero(message = "продолжительность должны быть положительной")
    private long duration;
}