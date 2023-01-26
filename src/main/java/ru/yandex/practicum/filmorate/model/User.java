package ru.yandex.practicum.filmorate.model;


import javax.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class User implements Item {
    private int id;
    @Email(message = "Некорректный email адрес")
    private String email;
    @NotBlank(message = "login не может быть пустой")
    @Pattern(regexp = ".*\\S.", message = "login не должен содержать пробелы")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}