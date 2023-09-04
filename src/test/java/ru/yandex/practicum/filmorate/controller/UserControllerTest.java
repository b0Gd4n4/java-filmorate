package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldNotAddUserWhenWrongEmailFormat() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"alone\",\n" +
                                "  \"name\": \"est a121212cing\",\n" +
                                "  \"email\": \"mailmail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(500));
    }


    @Test
    void shouldNotAddUserWhenLoginIsEmpty() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"\",\n" +
                                "  \"name\": \"est a324324icing\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(500));
    }

    @Test
    void shouldAddUserWhenNameIsEmpty() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content("{\n" +
                                        "  \"login\": \"nfdofdof\",\n" +
                                        "  \"name\": \"\",\n" +
                                        "  \"email\": \"mail@mail.ru\",\n" +
                                        "  \"birthday\": \"1946-08-20\"\n" +
                                        "}" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"nfdofdof\"")));
    }

    @Test
    void shouldNotAddUserWhenBirthdayIsInTheFuture() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"sdfsdf\",\n" +
                                "  \"name\": \"est adipisdsfs342icing\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"2946-08-20\"\n" +
                                "}" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(500));
    }
}
