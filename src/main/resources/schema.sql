DROP TABLE IF EXISTS users, films, mpa, genre, film_genre, film_likes, friends_user,
    director, film_director, reviews, reviews_likes, feed CASCADE;

CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    film_id      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(60)  NOT NULL,
    description  VARCHAR(200) NOT NULL,
    release_date DATE         NOT NULL,
    duration     INTEGER      NOT NULL,
    mpa_id       INTEGER REFERENCES mpa (mpa_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR(60) NOT NULL,
    login    VARCHAR(60) NOT NULL,
    name     VARCHAR(60) NOT NULL,
    birthday DATE        NOT NULL
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS director
(
    director_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    director_name VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_director
(
    director_id INTEGER REFERENCES director (director_id) ON DELETE CASCADE,
    film_id     INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS film_genre
(
    genre_id INTEGER REFERENCES genre (genre_id) ON DELETE CASCADE,
    film_id  INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friends_user
(
    user_id   INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews
(
    review_id      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    review_content VARCHAR,
    is_positive    BOOLEAN NOT NULL,
    user_id        INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    film_id        INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    useful         INTEGER DEFAULT 0,
    CONSTRAINT positive_id CHECK (user_id > 0 AND film_id > 0)
);

CREATE TABLE IF NOT EXISTS reviews_likes
(
    review_id INTEGER REFERENCES reviews (review_id) ON DELETE CASCADE,
    user_id   INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    is_like   BOOLEAN NOT NULL,
    CONSTRAINT reviews_likes_pk PRIMARY KEY (review_id, user_id)
);

CREATE TABLE IF NOT EXISTS feed
(
    event_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    timestamps BIGINT      NOT NULL,
    user_id    INTEGER     NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    event_type VARCHAR(10) NOT NULL,
    operation  VARCHAR(10) NOT NULL,
    entity_id  INTEGER     NOT NULL
);