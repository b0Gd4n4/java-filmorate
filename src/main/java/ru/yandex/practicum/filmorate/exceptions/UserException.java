package ru.yandex.practicum.filmorate.exceptions;

public class UserException extends RuntimeException {

    public UserException(String massage) {
        super(massage);
    }

}