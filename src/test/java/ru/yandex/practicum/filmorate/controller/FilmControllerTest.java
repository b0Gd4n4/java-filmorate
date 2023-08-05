package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @Test
    void shouldNotAddFilmWithEmptyName() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddFilmWithTooLongDescription() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"qqqqqqqqqqwwwwwwwwwweeeeeeeeeerrrrrrrrrrttttttttttyyyyyyyyyy" +
                                "qqqqqqqqqqqwwwwwwwwwweeeeeeeeeerrrrrrrrrrttttttttttyyyyyyyyyyqqqqqqqqqqwwwwwwwwww" +
                                "wwwwwwwwwweeeeeeeeeerrrrrrrrrrttttttttttyyyyyyyyyyqqqqqqqqqqqqqqqqqqqqqqqqqq\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddFilmWithTooOldDate() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"fdgfdg fdgdfgffdfgg\",\n" +
                                "  \"description\": \"dfgdfgfdgdf\",\n" +
                                "  \"releaseDate\": \"1400-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddFilmWithNegativeDuration() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"fgdfgfdgdf dfgdfgd\",\n" +
                                "  \"description\": \"afdgdgfdgfdng\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": -100\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}