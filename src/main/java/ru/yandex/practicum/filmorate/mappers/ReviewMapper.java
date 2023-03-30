package ru.yandex.practicum.filmorate.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class ReviewMapper implements RowMapper<Review> {

    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.debug("/mapRowReview");
        return Review.builder()
                .reviewId(rs.getInt("review_id"))
                .content(rs.getString("review_content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }
}
