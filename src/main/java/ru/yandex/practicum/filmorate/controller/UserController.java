package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.marker.Marker;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {
    private final UserIdGenerator userIdGenerator;

    final Map<Long, User> users = new HashMap<>();

    public UserController() {
        this.userIdGenerator = new UserIdGenerator();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validate(user);
        long id = userIdGenerator.getNextFreeId();
        user.setId(id);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь.");
        return user;
    }

    @PutMapping
    @Validated({Marker.Update.class})
    public User updateUser(@Valid @RequestBody User user) {
        if (users.get(user.getId()) != null) {
            validate(user);
            users.put(user.getId(), user);
            log.info("Запрос на изменение пользователя. Пользователь изменен.");
        } else {
            log.error("Запрос на изменение пользователя. Пользователь не найден.");
            throw new DataNotFoundException("Пользователь не найден.");
        }
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void validate(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());

        }
    }

    public static final class UserIdGenerator {
        private int nextFreeId = 0;


        public int getNextFreeId() {
            return ++nextFreeId;
        }
    }
}