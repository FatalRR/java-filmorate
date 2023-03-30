package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private final FriendStorage friendStorage;

    @Test
    void shouldSave() {
        User user = User.builder()
                .email("test@test.com")
                .login("TestLogin1")
                .name("TestName1")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        User saveUser = userDbStorage.save(user);
        assertEquals(user, saveUser);
    }

    @Test
    void shouldUpdate() {
        User user = User.builder()
                .email("test2@test.com")
                .login("TestLogin2")
                .name("TestName2")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        final User saveUser = userDbStorage.save(user);
        saveUser.setEmail("test3@test.com");
        saveUser.setLogin("TestLogin3");
        saveUser.setName("TestName3");
        saveUser.setBirthday(LocalDate.of(1997, 9, 9));

        final User updateUser = userDbStorage.update(saveUser);

        assertEquals(saveUser, updateUser);
    }

    @Test
    void shouldGetById() {
        User user = User.builder()
                .email("test4@test.com")
                .login("TeslLogin4")
                .name("TestName4")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        Integer userId = userDbStorage.save(user).getReviewId();
        User testUser = userDbStorage.getById(userId);

        assertEquals(user, testUser);
    }

    @Test
    void shouldSaveFriendAndGetFriend() {
        User user = User.builder()
                .email("test5@test.com")
                .login("TeslLogin5")
                .name("TestName5")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();
        User friendUser = User.builder()
                .email("test6@test.com")
                .login("TeslLogin6")
                .name("TestName6")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();

        Integer userId = userDbStorage.save(user).getReviewId();
        Integer friendUserId = userDbStorage.save(friendUser).getReviewId();

        friendStorage.addFriend(userId, friendUserId);

        List<User> testFriend = userDbStorage.getFriends(userId);

        assertEquals(1, testFriend.size());
        assertEquals(testFriend.get(0).getEmail(), friendUser.getEmail());
        assertEquals(testFriend.get(0).getBirthday(), friendUser.getBirthday());
    }

    @Test
    void shouldDeleteFriend() {
        User user = User.builder()
                .email("test7@test.com")
                .login("TeslLogin7")
                .name("TestName7")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();
        User friendUser = User.builder()
                .email("test8@test.com")
                .login("TeslLogin7")
                .name("TestName67")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();

        Integer userId = userDbStorage.save(user).getReviewId();
        Integer friendUserId = userDbStorage.save(friendUser).getReviewId();

        friendStorage.addFriend(userId, friendUserId);
        friendStorage.removeFriend(userId, friendUserId);

        List<User> testFriend = userDbStorage.getFriends(userId);

        assertEquals(0, testFriend.size());
    }

    @Test
    void shouldGetCorporateFriends() {
        User user1 = User.builder()
                .email("test9@test.com")
                .login("TeslLogin9")
                .name("TestName9")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();
        User user2 = User.builder()
                .email("test10@test.com")
                .login("TeslLogin10")
                .name("TestName10")
                .birthday(LocalDate.of(1991, 9, 9))
                .build();
        User user3 = User.builder()
                .email("test11@test.com")
                .login("TeslLogin11")
                .name("TestName11")
                .birthday(LocalDate.of(1992, 9, 9))
                .build();

        Integer user1Id = userDbStorage.save(user1).getReviewId();
        Integer user2Id = userDbStorage.save(user2).getReviewId();
        Integer user3Id = userDbStorage.save(user3).getReviewId();

        friendStorage.addFriend(user1Id, user3Id);
        friendStorage.addFriend(user2Id, user3Id);

        List<User> commonFriends = userDbStorage.getCorporateFriends(user1Id, user2Id);

        assertEquals(1, commonFriends.size());
        assertEquals(commonFriends.get(0).getEmail(), user3.getEmail());
        assertEquals(commonFriends.get(0).getBirthday(), user3.getBirthday());
    }

    @Test
    void shouldGetAll() {
        User user1 = User.builder()
                .email("test12@test.com")
                .login("TeslLogin12")
                .name("TestName12")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();
        User user2 = User.builder()
                .email("test13@test.com")
                .login("TeslLogin13")
                .name("TestName13")
                .birthday(LocalDate.of(1991, 9, 9))
                .build();
        userDbStorage.save(user1);
        userDbStorage.save(user2);
        List<User> getUsers = userDbStorage.getAll();
        assertEquals(2, getUsers.size());
    }
}