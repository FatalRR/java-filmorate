package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.*;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.validator.IsAfter;

import java.time.LocalDate;
import java.util.*;

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
    @IsAfter("1895-12-28")
    private LocalDate releaseDate;
    @PositiveOrZero(message = ExceptionMessages.POSITIVE_DURATION)
    private long duration;

    @NotNull
    private Mpa mpa;
    private final Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
    private final Set<Director> directors = new TreeSet<>(Comparator.comparingInt(Director::getId));

    public void addFilmGenre(Genre genre) {
        genres.add(genre);
    }

    public void addFilmDirectors(Director director) {
        directors.add(director);
    }
}