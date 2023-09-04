package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.marker.Marker;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Builder
@AllArgsConstructor
@Getter
@Setter
public class Film {
    @NotNull(groups = {Marker.Update.class})
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private final Set<Long> likes = new HashSet<>();

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public boolean removeLike(Long userId) {
        return likes.remove(userId);
    }

    public int numberOfLikes() {
        return likes.size();
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }
}
