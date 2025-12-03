package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/user")
    public void createUser(@RequestBody User user) {

        if (!user.getEmail().contains("@")) {
            throw new ValidExeption("email check '@'");
        }

        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidExeption("Login IS NULL / Login set ' '");
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidExeption("LocalDate is Birthday > now");
        }

        users.put(intIds(user.getId()), user);
        log.info("Пользователь создан");
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody User user) {
        users.put(user.getId(), user);
        log.info("Пользователь обнавлен");
    }


    public int intIds(int id) {
        return id++;
    }
}
