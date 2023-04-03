# java-filmorate

![img.png](img.png)

###### Таблица film
- ***film_id*** - уникальный идентификатор фильма
- ***name*** - название фильма
- ***description*** - писание фильма
- ***release_date*** - дата релиза фильма
- ***duration*** - продолжительность фильма
- ***mpa_id*** - идентификатор рейтинга фильма

###### Таблица user
- ***user_id*** - уникальный идентификатор пользователя
- ***email*** - электронная почта пользователя
- ***login*** - логин пользователя
- ***name*** - имя  пользователя
- ***birthday*** - дата рождения пользователя

###### Таблица film_likes
- ***film_id*** - идентификатор фильма
- ***user_id*** - идентификатор пользователя

###### Таблица mpa
- ***mpa_id*** - уникальный идентификатор рейтинга
- ***mpa_name*** - название рейтинга

###### Таблица genre
- ***genre_id*** - уникальный идентификатор жанра
- ***genre_name*** - название жанра

###### Таблица film_genre
- ***film_id*** - идентификатор фильма
- ***genre_id*** - идентификатор жанра

###### Таблица director
- ***director_id*** - уникальный идентификатор режиссера
- ***director_name*** - имя режиссера

###### Таблица film_director
- ***film_id*** - идентификатор фильма
- ***director_id*** - идентификатор режиссера

###### Таблица friends_user
- ***user_id*** - идентификатор пользователя
- ***friend_id*** - идентификатор друга

###### Таблица reviews
- ***reviews_id*** - уникальный идентификатор отзыва
- ***review_content*** - текст отзыва
- ***is_positive*** - положительный/отрицательный отзыв
- ***user_id*** - идентификатор пользователя
- ***film_id*** - идентификатор фильма
- ***useful*** - рейтинг полезности отзыва

###### Таблица reviews_likes
- ***reviews_id*** - уникальный идентификатор отзыва
- ***user_id*** - идентификатор пользователя
- ***is_like*** - название жанра

###### Таблица feed
- ***event_id*** - уникальный идентификатор события
- ***timestamps*** - время события
- ***user_id*** - идентификатор пользователя
- ***event_type*** - тип события, одно из значениий LIKE, REVIEW или FRIEND
- ***operation*** - тип операции, одно из значениий REMOVE, ADD, UPDATE
- ***entity_id*** - идентификатор сущности

--------------------
###### Реализованные фичи для групового проекта
Обязательные фичи:
- ***Удаление фильмов и пользователей***
- ***Добавление режиссёров в фильмы***
- ***Функциональность «Отзывы»***
- ***Функциональность «Поиск»***
- ***Функциональность «Рекомендации»***
- ***Функциональность «Лента событий»***

Дополнительные фичи:
- ***Функциональность «Общие фильмы»***
- ***Вывод самых популярных фильмов по жанру и годам***