package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.marker.Marker;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
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

}
