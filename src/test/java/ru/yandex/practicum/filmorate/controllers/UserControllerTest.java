package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest  {
    @Autowired
    UserController userController;

    @Autowired
    Storage<User> storage;

    @AfterEach
    public void clearUp() {
        storage.clearAll();
    }

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
        userController.save(user1);
        userController.save(user2);
        assertEquals(2, userController.getAll().size());
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        User user = User.builder()
                .email("test1@gmail.com")
                .login("testLogin1")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        userController.save(user);
        assertEquals("testLogin1",user.getName());
    }
}