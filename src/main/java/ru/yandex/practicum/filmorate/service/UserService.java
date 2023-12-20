package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.UserValidator.validateUser;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorageDb;


    public Collection<User> getAllUsers() {
        return userStorageDb.getAllUsers();
    }

    public User getUserById(Long id) {
        User user = userStorageDb.getUser(id);
        if (user == null) {
            throw new NotFoundException("User с id " + id + " не найден.");
        }
        return user;
    }

    public User createUser(User user) {
        validateUser(user);
        return userStorageDb.createUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        getUserById((long) Math.toIntExact(user.getId()));
        return userStorageDb.updateUser(user);
    }

    public void addFriend(Long idUser, Long idFriend) {
        getUserById(idUser);
        getUserById(idFriend);
        userStorageDb.addFriend(Math.toIntExact(idUser), Math.toIntExact(idFriend));
    }

    public void deleteFriend(Long idUser, Long idFriend) {
        getUserById(idUser);
        getUserById(idFriend);
        userStorageDb.deleteFriend(Math.toIntExact(idUser), Math.toIntExact(idFriend));
    }

    public List<User> getFriendsById(int idUser) {
        return userStorageDb.getFriends(idUser);
    }

    public List<User> haveCommonFriends(Long idUser, Long idFriend) {
        List<User> friendsUser = getFriendsById(Math.toIntExact(idUser));
        List<User> friendsFriend = getFriendsById(Math.toIntExact(idFriend));
        friendsUser.retainAll(friendsFriend);
        if (friendsUser.isEmpty()) {
            log.info("Не найдено общих друзей.");
        }
        return friendsUser;
    }


}
