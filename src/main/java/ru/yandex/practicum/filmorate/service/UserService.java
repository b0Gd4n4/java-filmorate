package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;


    public User createUser(User user) {
        validate(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getAllUsers();
    }


    public User findUserById(Long id) {
        return userStorage.getUserById(id);
    }


    public void addFriends(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
        log.info("Пользователи {} и {} теперь друзья", id, friendId);
    }

    public void removeFriends(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(id);
        log.info("Пользователи {} и {} больше не являются друзьями.", id, friendId);
    }

    public List<User> getAllFriends(Long id) {
        return userStorage.getUserById(id)
                .getFriends()
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        final User user = userStorage.getUserById(id);
        final User other = userStorage.getUserById(otherId);
        final Set<Long> friends = user.getFriends();
        final Set<Long> otherFriends = other.getFriends();

        return friends.stream()
                .filter(otherFriends::contains)
                .map(userId -> userStorage.getUserById(userId))
                .collect(Collectors.toList());
    }


    public void validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}

