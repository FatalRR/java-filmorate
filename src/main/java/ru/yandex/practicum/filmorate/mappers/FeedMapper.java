package ru.yandex.practicum.filmorate.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.EventTypes;
import ru.yandex.practicum.filmorate.enums.OperationTypes;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class FeedMapper implements RowMapper<Event> {
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.debug("/mapRowEvent");
        return Event.builder()
                .timestamp(rs.getLong("timestamps"))
                .userId(rs.getInt("user_id"))
                .eventType(EventTypes.valueOf(rs.getString("event_type")))
                .operation(OperationTypes.valueOf(rs.getString("operation")))
                .eventId(rs.getInt("event_id"))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}