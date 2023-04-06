package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.FilmSort;
import ru.yandex.practicum.filmorate.excepions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.mappers.FilmWithGenreAndDirectorMapper;
import ru.yandex.practicum.filmorate.messages.ExceptionMessages;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final FilmWithGenreAndDirectorMapper filmWithGenreAndDirectorMapper;

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name, d.director_id, d.director_name " +
                "FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN director AS d ON fd.director_id = d.director_id";

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
        addDirector(film);
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
        addDirector(film);
        return getById(film.getId());
    }

    @Override
    public Film getById(Integer id) {
        String sqlQuery = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name, d.director_id, d.director_name " +
                "FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN director AS d ON fd.director_id = d.director_id " +
                "WHERE f.film_id = " + id;

        List<Film> films = addFilm(sqlQuery);
        if (!films.isEmpty()) {
            return films.get(0);
        } else {
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);
        }
    }

    @Override
    public void removeById(Integer id) {
        String sqlQuery = "DELETE FROM films WHERE film_id = " + id;
        jdbcTemplate.update(sqlQuery);
    }

    public List<Film> getPopular(Integer count, Integer genreId, Integer year) {
        String sqlQuery = "SELECT f.*, m.mpa_name, GROUP_CONCAT(g.genre_id) as genre_id, GROUP_CONCAT(g.genre_name) as genre_name, d.director_id, d.director_name " +
                "FROM films AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS film_likes FROM film_likes GROUP BY film_id) AS l ON f.film_id = l.film_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN director AS d ON fd.director_id = d.director_id " +
                validateRequest(genreId, year) +
                "GROUP BY f.film_id " +
                "ORDER BY film_likes DESC " +
                "LIMIT " + count;

        return addFilm(sqlQuery);
    }

    @Override
    public List<Film> addFilm(String sqlQuery) {
        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, rs -> {
            Integer filmId = rs.getInt("film_id");
            if (!films.containsKey(filmId)) {
                Film film = filmMapper.mapRow(rs, filmId);
                films.put(filmId, film);
            }

            String genreIds = rs.getString("genre_id");
            String genreNames = rs.getString("genre_name");
            if (genreIds != null && genreNames != null) {
                String[] genreIdArray = genreIds.split(",");
                String[] genreNameArray = genreNames.split(",");
                IntStream.range(0, genreIdArray.length)
                        .forEach(i -> {
                            int genreId = Integer.parseInt(genreIdArray[i]);
                            String genreName = genreNameArray[i];
                            films.get(filmId).addFilmGenre(Genre.builder()
                                    .id(genreId)
                                    .name(genreName).build());
                        });
            }

            String directorName = rs.getString("director_name");
            if (directorName != null) {
                films.get(filmId).addFilmDirectors(Director.builder()
                        .id(rs.getInt("director_id"))
                        .name(directorName).build());
            }
        });
        return new ArrayList<>(films.values());
    }

    private void addGenre(Film film) {
        Integer filmId = film.getId();
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        List<Genre> genresList = new ArrayList<>(film.getGenres());
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

    private void addDirector(Film film) {
        Integer filmId = film.getId();
        jdbcTemplate.update("DELETE FROM film_director WHERE film_id = ?", filmId);
        List<Director> directorList = new ArrayList<>(film.getDirectors());
        String addDirectorsQuery = "MERGE INTO film_director (film_id, director_id) " +
                "VALUES (?,?)";
        jdbcTemplate.batchUpdate(addDirectorsQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, filmId);
                ps.setInt(2, directorList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return directorList.size();
            }
        });
    }

    @Override
    public List<Film> getDirectorFilm(Integer directorId, FilmSort sortBy) {
        switch (sortBy) {
            case likes:
                String sqlLikesQuery = "SELECT * FROM films AS f " +
                        "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                        "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                        "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                        "LEFT JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                        "LEFT JOIN film_director AS fd ON f.film_id=fd.film_id " +
                        "LEFT JOIN director AS d ON fd.director_id = d.director_id " +
                        "WHERE d.director_id = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY COUNT(fl.film_id) DESC";

                if (jdbcTemplate.query(sqlLikesQuery, filmWithGenreAndDirectorMapper, directorId).isEmpty()) {
                    throw new NotFoundException(String.valueOf(LogMessages.MISSING));
                }
                return jdbcTemplate.query(sqlLikesQuery, filmWithGenreAndDirectorMapper, directorId);
            case year:
                String sqlYearsQuery = "SELECT * FROM films AS f " +
                        "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                        "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                        "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                        "LEFT JOIN film_director AS fd ON f.film_id=fd.film_id " +
                        "LEFT JOIN director AS d ON fd.director_id = d.director_id " +
                        "WHERE d.director_id = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY f.release_date";

                if (jdbcTemplate.query(sqlYearsQuery, filmWithGenreAndDirectorMapper, directorId).isEmpty()) {
                    throw new NotFoundException(String.valueOf(LogMessages.MISSING));
                }

                return jdbcTemplate.query(sqlYearsQuery, filmWithGenreAndDirectorMapper, directorId);
            default:
                return new ArrayList<>();
        }
    }

    public List<Film> getCommon(Integer userId, Integer friendId) {
        String sqlRequst = "SELECT t2.film_id " +
                "FROM film_likes t1, film_likes t2 " +
                "WHERE t1.film_id = t2.film_id AND t1.user_id != t2.user_id " +
                "AND t1.user_id = ? " +
                "AND t2.user_id = ? " +
                "GROUP BY t2.film_id " +
                "ORDER BY count(t2.film_id) DESC";

        List<Integer> filmIds = jdbcTemplate.queryForList(sqlRequst, Integer.class, userId, friendId);

        List<Film> films = new ArrayList<>();
        filmIds.stream().forEach(id -> films.add(getById(id)));

        return films;
    }

    private String validateRequest(Integer genreId, Integer year) {
        String sqlQuery = null;
        if (genreId != null && year == null) {
            sqlQuery = "LEFT JOIN (SELECT film_id FROM film_genre WHERE genre_id = " + genreId + ") AS wh ON f.film_id = wh.film_id " +
                    "WHERE f.film_id = wh.film_id ";
        } else if (genreId == null && year != null) {
            sqlQuery = "WHERE EXTRACT(YEAR FROM CAST(f.release_date AS date)) = " + year + " ";
        } else if (genreId != null && year != null) {
            sqlQuery = "LEFT JOIN (SELECT film_id FROM film_genre WHERE genre_id = " + genreId + ") AS wh ON f.film_id = wh.film_id " +
                    "WHERE f.film_id = wh.film_id " +
                    "AND EXTRACT(YEAR FROM CAST(f.release_date AS date)) = " + year + " ";
        } else if (genreId == null && year == null) {
            sqlQuery = " ";
        }

        return sqlQuery;
    }

    public List<Film> recommendations(Integer userId) {
        String sqlRequst = "SELECT t3.film_id " +
                "FROM film_likes t3 " +
                "WHERE t3.user_id IN (SELECT t2.user_id " +
                "FROM film_likes t1, film_likes t2 " +
                "WHERE t1.film_id = t2.film_id " +
                "AND t1.user_id != t2.user_id " +
                "AND t1.user_id = ? " +
                "GROUP BY t2.user_id " +
                "ORDER BY count(t2.film_id) DESC) " +
                "AND t3.film_id NOT IN (" +
                "SELECT t4.film_id " +
                "FROM film_likes t4 " +
                "WHERE t4.user_id = ?)";

        List<Integer> filmIds = jdbcTemplate.queryForList(sqlRequst, Integer.class, userId, userId);
        List<Film> films = new ArrayList<>();
        filmIds.forEach(id -> films.add(getById(id)));

        return films;
    }
}