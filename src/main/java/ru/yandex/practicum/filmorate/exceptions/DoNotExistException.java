package ru.yandex.practicum.filmorate.exceptions;

public class DoNotExistException extends RuntimeException {
    public DoNotExistException(String message) {
        super(message);
    }
}

