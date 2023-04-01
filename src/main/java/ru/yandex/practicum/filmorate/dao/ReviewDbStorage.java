package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate, ReviewMapper reviewMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Review save(Review review) {
        String sqlQuery = "INSERT INTO reviews (review_content, is_positive, user_id, film_id)" +
                "values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setInt(3, review.getUserId());
            stmt.setInt(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        int reviewId = keyHolder.getKey().intValue();
        return getById(reviewId);
    }

    @Override
    public Review update(Review review) {
        checkReviewExists(review.getReviewId());
        String sql = "UPDATE reviews SET review_content = ?, is_positive = ? WHERE review_id = ?";

        jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());

        return getById(review.getReviewId());
    }

    @Override
    public void delete(Integer id) {
        checkReviewExists(id);
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Review getById(Integer id) {
        checkReviewExists(id);
        String sql = "SELECT * FROM reviews WHERE review_id = ?";
        return jdbcTemplate.queryForObject(sql, reviewMapper, id);
    }

    @Override
    public List<Review> getFilmReviews(Optional<Integer> filmId, Integer count) {
        if (filmId.isEmpty()) {
            return getAllReviews();
        }
        String sql = "SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC LIMIT ?";

        return jdbcTemplate.query(sql, reviewMapper, filmId.get(), count);
    }

    @Override
    public List<Review> getAllReviews() {
        String sql = "SELECT * FROM reviews ORDER BY useful DESC";
        return jdbcTemplate.query(sql, reviewMapper);
    }

    @Override
    public void changeUseful(Integer reviewId, Boolean isUsefulIncreasing) {
        String sql;
        if (isUsefulIncreasing) {
            sql = "UPDATE reviews SET useful = useful + 1 WHERE review_id = ?";
        } else {
            sql = "UPDATE reviews SET useful = useful - 1 WHERE review_id = ?";
        }
        jdbcTemplate.update(sql, reviewId);
    }

    public void checkReviewExists(Integer id) {
        String sql = "SELECT * FROM reviews WHERE review_id = ?";
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(sql, id);
        if (!reviewRows.next()) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }
    }
}
