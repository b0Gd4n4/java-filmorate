package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.Set;

public interface UserStorage {


    User addUser(User user);


    User updateUser(Integer id, User user);


    User getUserById(Integer id) throws SQLException;


    Set<User> getAllUsers();


}
