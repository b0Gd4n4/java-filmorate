package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.marker.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    private Collection<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей.");
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    private User findUserById(@PathVariable Long id) {
        log.info("Запрос на получения пользователя = {} по id", id);
        return userService.getUserById(id);
    }

    @GetMapping(value = "/{id}/friends")
    private List<User> getFriends(@PathVariable Long id) {
        return userService.getFriendsById(Math.toIntExact(id));
    }

    @GetMapping(value = "/{id}/friends/common/{friendId}")
    private List<User> haveCommonFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получения общих друзей пользователя {} и {}", id, friendId);
        return userService.haveCommonFriends(id, friendId);
    }

    @PostMapping
    private User createUser(@Valid @RequestBody User user) {
        log.info("Добавлен новый пользователь.");
        return userService.createUser(user);
    }

    @PutMapping
    private User updateUser(@Validated({Marker.Update.class}) @RequestBody User user) {
        log.info("Запрос на изменение фильма. Фильм изменён.");
        return userService.updateUser(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    private void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователи {} и {} теперь друзья", id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    private void removeFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Пользователи {} и {} больше не являются друзьями.", id, friendId);
    }
}