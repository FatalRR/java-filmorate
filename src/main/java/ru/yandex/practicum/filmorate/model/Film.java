package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film extends  Entity {
    private int id;
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    private String name;
    @Size(max = 200, message = ExceptionMessages.MAX_DESCRIPTION)
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero(message = ExceptionMessages.POSITIVE_DURATION)
    private long duration;
    @JsonIgnore
    private final Set<Integer> likes = new HashSet<>();
}