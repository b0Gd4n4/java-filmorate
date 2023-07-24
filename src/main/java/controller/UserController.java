package controller;


import exceptions.UserException;
import exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserIdGenerator userIdGenerator;
    //private static final Logger log = LoggerFactory.getLogger(UserController.class);
    final Map<Integer, User> users = new HashMap<>();

    public UserController() {
        this.userIdGenerator = new UserIdGenerator();
    }

    @PostMapping(value = "/user")
    public User createUser(@Valid @RequestBody User user) {
        isValid(user);
        int id = userIdGenerator.getNextFreeId();
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен новый пользователь.");
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.get(user.getId()) != null) {
            isValid(user);
            users.put(user.getId(), user);
            log.info("Запрос на изменение пользователя. Пользователь изменен.");
        } else {
            log.error("Запрос на изменение пользователя. Пользователь не найден.");
            throw new UserException("Пользователь не найден.");
        }
        return user;
    }


    private void isValid(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    public static final class UserIdGenerator {
        private int nextFreeId = 0;


        public int getNextFreeId() {
            return nextFreeId++;
        }
    }
}