# java-filmorate

ERD: https://app.quickdatabasediagrams.com/#/d/S0dVKo
![2023-03-05_18-55-00](https://user-images.githubusercontent.com/113539431/222958929-7cfa159d-447b-43a4-b66a-663a1f4b2268.png)

###### Таблица film
- ***id*** - уникальный индентифиатор фильма  
- ***name*** - название фильма  
- ***description*** - писание фильма  
- ***release_date*** - дата релиза фильма  
- ***duration*** - продолжительность фильма  
- ***mpa_id*** - идетификатор рейтинга фильма  

###### Таблица user
- ***id*** - уникальный идентифиакатор пользователя  
- ***email*** - электронная почта пользователя  
- ***login*** - логин пользователя  
- ***name*** - имя  пользователя  
- ***birthday*** - дата рождения пользователя   

###### Таблица film_likes
- ***film_id*** - идентификатор фильма  
- ***user_id*** - идентификатор пользователя   

###### Таблица mpa
- ***id*** - уникальный идентификатор рейтинга  
- ***name*** - название рейтинга  

###### Таблица genre
- ***id*** - уникальный идетификатор жанра  
- ***name*** - название жанра  

###### Таблица film genre
- ***film_id*** - идентификатор фильма  
- ***genre_id*** - идентификатор жанра 


<!-- Оформление посмотрел, все хорошо, но можно добавить сортировку с опсание таблиц по алфавиту
