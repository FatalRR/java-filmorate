package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends UserController {
    UserController userController = new UserController();

    @Test
    void shouldGetAllUsers() {
        User user1 = User.builder()
                .name("userName1")
                .email("test1@gmail.com")
                .login("testLogin1")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        User user2 = User.builder()
                .name("userName2")
                .email("test1@gmail.com")
                .login("testLogin1")
                .birthday(LocalDate.of(2022, 2, 2))
                .build();
        userController.create(user1);
        userController.create(user2);
        assertEquals(2, userController.findAll().size());
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        User user = User.builder()
                .email("test1@gmail.com")
                .login("testLogin1")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        userController.create(user);
        assertEquals("testLogin1",user.getName());
    }
}