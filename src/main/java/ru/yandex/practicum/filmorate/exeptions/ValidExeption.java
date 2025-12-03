package ru.yandex.practicum.filmorate.exeptions;

public class ValidExeption extends RuntimeException {

    public ValidExeption() {
    }

    public ValidExeption(final String message) {
        super(message);
    }

    public ValidExeption(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ValidExeption(final Throwable cause) {
        super(cause);
    }
}
