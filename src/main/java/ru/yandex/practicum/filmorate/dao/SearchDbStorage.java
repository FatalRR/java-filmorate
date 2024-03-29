package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.FilmSearch;
import ru.yandex.practicum.filmorate.excepions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.SearchStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.messages.ExceptionMessages.INCORRECT_PAR;

@Repository
@RequiredArgsConstructor
public class SearchDbStorage implements SearchStorage {
    private final FilmStorage filmStorage;

    @Override
    public List<Film> getSearch(String query, String searchBy) {
        String sqlQueryDirector = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name, d.director_id, d.director_name " +
                "FROM films AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS film_likes " +
                "FROM film_likes GROUP BY film_id) AS l ON f.film_id = l.film_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN director AS d ON fd.director_id = d.director_id " +
                validateRequest(query, searchBy) +
                "GROUP BY g.genre_id, f.name " +
                "ORDER BY film_likes DESC";
        return filmStorage.addFilm(sqlQueryDirector)
                .stream()
                .sorted(Comparator.comparing(Film::getId)
                        .reversed())
                .collect(Collectors.toList());
    }

    private String validateRequest(String query, String searchBy) {
        List<String> requestParam = List.of(searchBy.split(","));
        if (requestParam.size() == 1) {
            switch (requestParam.get(0)) {
                case FilmSearch.DIRECTOR:
                    return "WHERE LOWER(d.director_name) LIKE " + "LOWER('%" + query + "%') ";
                case FilmSearch.TITLE:
                    return "WHERE LOWER(f.name) LIKE " + "LOWER('%" + query + "%') ";
            }
        } else if (requestParam.size() == 2) {
            return "WHERE LOWER(d.director_name) LIKE " + "LOWER('%" + query + "%') OR LOWER(f.name) LIKE " + "LOWER('%" + query + "%') ";
        } else {
            throw new ValidationException(INCORRECT_PAR);
        }
        return new String();
    }
}