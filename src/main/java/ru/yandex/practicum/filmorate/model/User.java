package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

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
public class User {
    private int id;
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    private String email;
    @NotBlank(message = ExceptionMessages.EMPTY_LOGIN)
    @Pattern(regexp = ".*\\S.", message = ExceptionMessages.LOGIN_WITHOUT_SPACE)
    private String login;
    private String name;
    @PastOrPresent(message = ExceptionMessages.INCORRECT_BIRTHDAY)
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
}