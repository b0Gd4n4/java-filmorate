package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("inMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 0;

    @Override
    public Long getNextFreeId() {
        return ++nextId;
    }

    @Override
    public User createUser(User user) {
        long id = getNextFreeId();
        user.setId(id);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new DataNotFoundException("Ключ не найден: " + user.getId());
        } else {
            users.put(user.getId(), user);
            log.info("Запрос на изменение фильма. Фильм изменён.");
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id))
            throw new DataNotFoundException(String.format("Request user with absent id = %d", id));

        return users.get(id);
    }
}
