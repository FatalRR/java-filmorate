package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.SearchStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.messages.ExceptionMessages.INCORRECT_PAR;

@Repository
public class SearchDbStorage implements SearchStorage {
    private final FilmStorage filmStorage;

    @Autowired
    public SearchDbStorage(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public List<Film> getSearch(String query, String by) {
        String sqlQueryDirector = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name, d.director_id, d.director_name " +
                "FROM films AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS film_likes " +
                "FROM film_likes GROUP BY film_id) AS l ON f.film_id = l.film_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN director AS d ON fd.director_id = d.director_id " +
                validateRequest(query, by) +
                "GROUP BY g.genre_id, f.name " +
                "ORDER BY film_likes DESC";
        return filmStorage.addFilm(sqlQueryDirector).stream().sorted(Comparator.comparing(Film::getId).reversed()).collect(Collectors.toList());
    }

    private String validateRequest(String query, String by) {
        List<String> requestParam = List.of(by.split(","));
        if (requestParam.size() == 1) {
            switch (requestParam.get(0)) {
                case "director":
                    return "WHERE LOWER(d.director_name) LIKE " + "LOWER('%" + query + "%') ";
                case "title":
                    return "WHERE LOWER(f.name) LIKE " + "LOWER('%" + query + "%') ";
            }
        } else if(requestParam.size() == 2) {
            return "WHERE LOWER(d.director_name) LIKE " + "LOWER('%" + query + "%') OR LOWER(f.name) LIKE " + "LOWER('%" + query + "%') ";
        } else {
            throw new ValidationException(INCORRECT_PAR);
        }
        return new StringBuilder().toString();
    }
}
