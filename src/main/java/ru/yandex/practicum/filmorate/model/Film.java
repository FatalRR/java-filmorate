package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.*;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class Film extends Entity {
    private Integer id;
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    private String name;
    @Size(max = 200, message = ExceptionMessages.MAX_DESCRIPTION)
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero(message = ExceptionMessages.POSITIVE_DURATION)
    private long duration;
    @NotNull
    private int rate;
    @NotNull
    private Mpa mpa;
    private final List<Genre> genres = new ArrayList<>();

    public void addFilmGenre(Genre genre) {
        genres.add(genre);
    }
}