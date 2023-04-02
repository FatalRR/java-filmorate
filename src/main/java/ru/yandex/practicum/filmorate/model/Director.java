package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
public class Director extends Entity {
    private Integer id;
    @NotBlank
    private String name;
}