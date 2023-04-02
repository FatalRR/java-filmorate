package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.EventTypes;
import ru.yandex.practicum.filmorate.enums.OperationTypes;
import ru.yandex.practicum.filmorate.mappers.FeedMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.user.FeedStorage;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class FeedDbStorage implements FeedStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FeedMapper feedMapper;

    @Autowired
    public FeedDbStorage(JdbcTemplate jdbcTemplate, FeedMapper feedMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.feedMapper = feedMapper;
    }

    @Override
    public List<Event> getByUserId(Integer userId) {
        String sqlQuery = "SELECT f.event_id, " +
                "f.timestamps, " +
                "f.user_id, " +
                "f.event_type, " +
                "f.operation, " +
                "f.entity_id " +
                "FROM feed AS f " +
                "WHERE f.user_id = ?;";
        return jdbcTemplate.query(sqlQuery, feedMapper, userId);
    }

    @Override
    public Event addEvent(Integer userId, EventTypes eventTypes, OperationTypes operationTypes, Integer entityId) {
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(userId)
                .eventType(eventTypes)
                .operation(operationTypes)
                .entityId(entityId)
                .eventId(0)
                .build();

        String sqlQuery = "INSERT INTO feed (timestamps, user_id, event_type, operation, entity_id) " +
                "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"event_id"});
            statement.setLong(1, event.getTimestamp());
            statement.setInt(2, event.getUserId());
            statement.setString(3, event.getEventType().toString());
            statement.setString(4, event.getOperation().toString());
            statement.setInt(5, event.getEntityId());
            return statement;
        }, keyHolder);
        event.setEventId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return event;
    }
}