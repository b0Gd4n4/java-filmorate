package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.marker.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        log.info("Добавлен новый пользователь.");
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@Validated({Marker.Update.class}) @RequestBody User user) {
        log.info("Запрос на изменение фильма. Фильм изменён.");
        return userService.updateUser(user);
    }


    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей.");
        return userService.getUsers();
    }


    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Long id) {
        log.info("Запрос на получения пользователя = {} по id", id);
        return userService.findUserById(id);
    }


    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriends(id, friendId);
        log.info("Пользователи {} и {} теперь друзья", id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriends(id, friendId);
        log.info("Пользователи {} и {} больше не являются друзьями.", id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Запрос на получение всех пользователей.");
        return userService.getAllFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        List<User> first = userService.getAllFriends(id);
        List<User> second = userService.getAllFriends(otherId);
        log.info("Общие друзья пользователя {} и {}", id, otherId);
        return first.stream().filter(second::contains).collect(Collectors.toList());
    }


}