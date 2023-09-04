package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.marker.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Builder
@AllArgsConstructor
@Setter
@Getter
public class User {
    @NotNull(groups = {Marker.Update.class})
    private Long id;
    @Email
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();

    public void addFriend(Long userId) {
        friends.add(userId);
    }

    public boolean removeFriend(Long userId) {
        return friends.remove(userId);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
