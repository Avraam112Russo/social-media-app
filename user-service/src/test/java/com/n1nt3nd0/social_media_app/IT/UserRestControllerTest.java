package com.n1nt3nd0.social_media_app.IT;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {
    @Autowired
    private MockMvc mockMvc;



    @Test
    @Disabled
    public void saveUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"Pasha\"," +
                                " \"lastName\": \"Sakharov\"," +
                                " \"email\": \"rus@mail.ru\", " +
                                "\"birthday\": \"2000-08-16\"  }")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    @Test
    @Disabled
    public void getUserByIdTest_successfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Pasha"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Disabled
    @Test
    public void getUserByIdTest_throwException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/user/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User %s not found".formatted(2L)))
                .andDo(MockMvcResultHandlers.print());
    }
}
