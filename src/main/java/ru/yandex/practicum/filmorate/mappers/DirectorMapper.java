package ru.yandex.practicum.filmorate.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class DirectorMapper implements RowMapper<Director> {

    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.debug("/mapRowDirector");
        return Director.builder()
                .id(rs.getInt("director_id"))
                .name(rs.getString("director_name"))
                .build();
    }
}
