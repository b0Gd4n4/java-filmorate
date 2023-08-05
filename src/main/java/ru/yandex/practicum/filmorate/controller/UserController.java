package ru.yandex.practicum.filmorate.controller;


import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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
        return userService.getUsers();
    }


    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Long id) {
        log.info("Request user by id = {}", id);
        return userService.findUserById(id);
    }


    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriends(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        List<User> first = userService.getAllFriends(id);
        List<User> second = userService.getAllFriends(otherId);
        return first.stream().filter(second::contains).collect(Collectors.toList());
    }


}