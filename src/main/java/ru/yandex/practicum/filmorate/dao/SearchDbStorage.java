package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.SearchStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SearchDbStorage implements SearchStorage {

    private final JdbcTemplate jdbcTemplate;

    private final FilmMapper filmMapper;

    @Autowired
    public SearchDbStorage(JdbcTemplate jdbcTemplate, FilmMapper filmMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMapper = filmMapper;
    }

    @Override
    public List<Film> getSearch(String query, String by) {
        List<String> requestParam = List.of(by.split(","));
        if (requestParam.size() == 1) {
            switch (requestParam.get(0)) {
                case "director":
                    String sqlQueryDirector = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name, d.director_id, d.director_name " +
                            "FROM films AS f " +
                            "LEFT JOIN (SELECT film_id, COUNT(user_id) AS film_likes " +
                            "FROM film_likes GROUP BY film_id) AS l ON f.film_id = l.film_id " +
                            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                            "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                            "LEFT JOIN director AS d ON fd.director_id = d.director_id " +
                            "WHERE LOWER(d.director_name) LIKE " + "LOWER('%" + query + "%') " +
                            "GROUP BY g.genre_id, f.name " +
                            "ORDER BY film_likes DESC";
                    return addFilm(sqlQueryDirector);

                case "title":
                    String sqlQueryTitle = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name, d.director_id, d.director_name " +
                            "FROM films AS f " +
                            "LEFT JOIN (SELECT film_id, COUNT(user_id) AS film_likes " +
                            "FROM film_likes GROUP BY film_id) AS l ON f.film_id = l.film_id " +
                            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                            "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                            "LEFT JOIN director AS d ON fd.director_id = d.director_id " +
                            "WHERE LOWER(f.name) LIKE " + "LOWER('%" + query + "%') " +
                            "GROUP BY g.genre_id, f.name " +
                            "ORDER BY film_likes DESC";
                    return addFilm(sqlQueryTitle);
            }

        } else {
            String sqlQueryDT = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name, d.director_id, d.director_name " +
                    "FROM films AS f " +
                    "LEFT JOIN (SELECT film_id, COUNT(user_id) AS film_likes " +
                    "FROM film_likes GROUP BY film_id) AS l ON f.film_id = l.film_id " +
                    "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                    "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                    "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                    "LEFT JOIN director AS d ON fd.director_id = d.director_id " +
                    "WHERE LOWER(d.director_name) LIKE " + "LOWER('%" + query + "%') " +
                    "OR LOWER(f.name) LIKE " + "LOWER('%" + query + "%') " +
                    "GROUP BY g.genre_id, f.name " +
                    "ORDER BY film_likes DESC";
            return addFilm(sqlQueryDT).stream().sorted(Comparator.comparing(Film::getId).reversed()).collect(Collectors.toList());
        }
        return null;
    }

    private List<Film> addFilm(String sqlQuery) {
        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, rs -> {
            Integer filmId = rs.getInt("film_id");
            if (!films.containsKey(rs.getInt("film_id"))) {
                Film film = filmMapper.mapRow(rs, filmId);
                films.put(filmId, film);
            }

            if (rs.getString("genre_name") != null) {
                films.get(filmId).addFilmGenre(Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre_name")).build());
            }

            if (rs.getString("director_name") != null) {
                films.get(filmId).addFilmDirectors(Director.builder()
                        .id(rs.getInt("director_id"))
                        .name(rs.getString("director_name")).build());
            }
        });
        return new ArrayList<>(films.values());
    }
}
