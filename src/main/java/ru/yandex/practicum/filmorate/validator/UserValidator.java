package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class UserValidator {

    @ResponseBody
    public static void validateUser(User user) {
        try {

            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }

        } catch (NullPointerException ex) {
            log.info("Поля пользователя не могут быть null.");
            throw new ValidationException("Поля пользователя не могут быть null.");
        }
    }
}