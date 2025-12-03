package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeptions.ValidExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    UserController userController = new UserController();

    @Test
    public void nullName() {

        User user = new User(1, "sdfffd23", "dsdsds", "Fdfdf", LocalDate.of(2003, 3, 2));
        final ValidExeption exception = assertThrows(

                ValidExeption.class,

                new Executable() {
                    @Override
                    public void execute() {
                        userController.createUser(user);
                    }
                });

        System.out.println(exception.getMessage());

        assertEquals("email check '@'", exception.getMessage());
    }

    @Test
    public void getLoginIsNull() {

        User user = new User(1, "sdfffd23@", "sdsddfdf", "Fdfdf", LocalDate.of(2003, 3, 2));
        final ValidExeption exception = assertThrows(

                ValidExeption.class,

                new Executable() {
                    @Override
                    public void execute() {
                        userController.createUser(user);
                    }
                });

        System.out.println(exception.getMessage());

        assertEquals("Login IS NULL / Login set ' '", exception.getMessage());
    }

    @Test
    public void getBirthdayIsAfter() {

        User user = new User(1, "sdfffd23@", "fdfddffdfd", "Fdfdf", LocalDate.of(2026, 3, 2));
        final ValidExeption exception = assertThrows(

                ValidExeption.class,

                new Executable() {
                    @Override
                    public void execute() {
                        userController.createUser(user);
                    }
                });

        System.out.println(exception.getMessage());

        assertEquals("LocalDate is Birthday > now", exception.getMessage());
    }
}
