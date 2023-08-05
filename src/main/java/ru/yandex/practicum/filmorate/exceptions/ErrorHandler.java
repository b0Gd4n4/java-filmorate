package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleFilmNotFoundException(final DataNotFoundException e) {
        log.warn(e.getMessage());
        return new Error(
                e.getMessage()
        );
    }



    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleValidationException(final ValidationException e) {
        log.warn(e.getMessage());
        return new Error(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleRunTimeException(final RuntimeException e) {
        log.warn(e.getMessage());
        return new Error(
                e.getMessage()
        );
    }

    public class Error {
        private final String error;

        public Error(String error) {
            this.error = error;
        }
        public String getError() {
            return error;
        }
    }


}