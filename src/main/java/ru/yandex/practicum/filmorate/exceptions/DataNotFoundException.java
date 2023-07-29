package ru.yandex.practicum.filmorate.exceptions;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String massage) {
        super(massage);
    }

}
