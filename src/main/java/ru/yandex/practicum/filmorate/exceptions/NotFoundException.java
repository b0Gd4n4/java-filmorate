package ru.yandex.practicum.filmorate.exceptions;

/*import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String message) {
        super(message);
        log.warn(message);
    }
}*/
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}