package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.marker.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@Builder
@Data
public class User {
    @NotNull(groups = {Marker.Update.class})
    private Long id;
    @Email
    private final String email;
    @NonNull
    @NotBlank
    private final String login;
    private String name;
    @Past
    private final LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();


}