package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, userMapper);
    }

    @Override
    public User save(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday)" +
                "values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            user.setId((Integer) keyHolder.getKey());
        }
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        return user;
    }

    @Override
    public User getById(Integer id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, userMapper, id);
    }

    @Override
    public void removeById(Integer id) {
        String sqlQuery = "DELETE FROM users WHERE user_id = " + id;
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<User> getFriends(Integer id) {
        String sqlQuery = "SELECT * FROM users AS u " +
                "JOIN (SELECT friend_id " +
                "FROM friends_user " +
                "WHERE user_id = ?) AS f " +
                "ON u.user_id = f.friend_id";

        String checkQuery = "SELECT COUNT(*) FROM users WHERE user_id = ?";

        if (jdbcTemplate.queryForObject(checkQuery, Integer.class, id) == 0) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден");
        }

        return jdbcTemplate.query(sqlQuery, userMapper, id);
    }

    @Override
    public List<User> getCorporateFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT * FROM users AS u " +
                "JOIN (SELECT friend_id FROM friends_user WHERE user_id = ?) AS fr " +
                "ON u.user_id = fr.friend_id " +
                "JOIN (SELECT friend_id FROM friends_user WHERE user_id = ?) AS nfr " +
                "ON u.user_id = nfr.friend_id";
        return jdbcTemplate.query(sqlQuery, userMapper, id, otherId);
    }
}