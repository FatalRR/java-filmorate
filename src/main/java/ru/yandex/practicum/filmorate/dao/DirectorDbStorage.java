package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.DirectorStorage;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@Slf4j
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorMapper directorMapper;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbcTemplate, DirectorMapper directorMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.directorMapper = directorMapper;
    }

    @Override
    public List<Director> getAll() {
        String sqlQuery = "SELECT * FROM director";
        return jdbcTemplate.query(sqlQuery, directorMapper);
    }

    @Override
    public Director getById(Integer directorId) {
        String sqlQuery = "SELECT * FROM director WHERE director_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, directorMapper, directorId);
    }

    @Override
    public Director save(Director director) {
        String sqlQuery = "INSERT INTO director (director_name) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            director.setId((Integer) keyHolder.getKey());
        }
        return director;
    }

    @Override
    public Director update(Director director) {
        String sqlQuery = "UPDATE director SET director_name = ? WHERE director_id = ?";
        if (jdbcTemplate.update(sqlQuery, director.getName(), director.getId()) == 0) {
            String message = "Режиссер " + director + " не найден.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        return director;
    }

    @Override
    public void removeById(Integer id) {
        String sqlQuery = "DELETE FROM director WHERE director_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}