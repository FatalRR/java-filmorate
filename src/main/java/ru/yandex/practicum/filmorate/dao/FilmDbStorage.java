package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMapper filmMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMapper = filmMapper;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name " +
                "FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id";

        return addFilm(sqlQuery);
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
                "values(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            film.setId((Integer) keyHolder.getKey());
        }
        addGenre(film);
        return getById(film.getId());
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        addGenre(film);
        return getById(film.getId());
    }

    @Override
    public Film getById(Integer id) {
        String sqlQuery = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name " +
                "FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id = " + id;

        List<Film> films = addFilm(sqlQuery);
        if (!films.isEmpty()) {
            return films.get(0);
        } else {
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);
        }
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO film_likes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM film_likes " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public List<Film> getPopular(Integer count) {
        String sqlQuery = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name FROM films AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS film_likes " +
                "FROM film_likes GROUP BY film_id) AS l ON f.film_id = l.film_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "ORDER BY film_likes DESC " +
                "LIMIT " + count;

        return addFilm(sqlQuery);
    }

    private List<Film> addFilm(String sqlQuery) {
        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, rs -> {
            Integer filmId = rs.getInt("film_id");
            if (!films.containsKey(filmId)) {
                Film film = filmMapper.mapRow(rs, filmId);
                films.put(filmId, film);
            }
            String genres_name = rs.getString("genre_name");
            if (genres_name != null) {
                films.get(filmId).addFilmGenre(Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(genres_name).build());
            }
        });
        return new ArrayList<>(films.values());
    }

    private void addGenre(Film film) {
        Integer filmId = film.getId();
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        List<Genre> genresList = film.getGenres();
        String addGenresQuery = "MERGE INTO film_genre (film_id, genre_id) " +
                "VALUES (?,?)";
        jdbcTemplate.batchUpdate(addGenresQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, filmId);
                ps.setInt(2, genresList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genresList.size();
            }
        });
    }
}