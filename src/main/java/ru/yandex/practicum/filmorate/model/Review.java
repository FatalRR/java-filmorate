package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class Review extends Entity {

    private Integer id;

    @NotNull
    @NotBlank
    private String content;

    @NotNull
    private Boolean isPositive;

    @Positive
    private Integer userId;

    @Positive
    private Integer filmId;

    private Integer useful;
}
