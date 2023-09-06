package ru.yandex.practicum.filmorate.model;


import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.marker.Marker;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Builder
@AllArgsConstructor
@Getter
@Setter
@Jacksonized
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
    private Integer duration;
    private Integer rate;
    private MPA mpa;
    private List<Genre> genres;
    private Set<Integer> likes;

    public void addLike(Long userId) {
        likes.add(Math.toIntExact(userId));
    }

    public boolean removeLike(Long userId) {
        return likes.remove(userId);
    }

    public int numberOfLikes() {
        return likes.size();
    }


}