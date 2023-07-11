package com.api.note.integration.controllers.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;
import com.api.note.repositories.EmailNumberRepository;
import com.api.note.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserDemoControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailNumberRepository emailNumberRepository;

    @Test
    public void esperoQueRetorneUmCodigoDeStatus200ComOsDadosDosTresUsuariosDemo() throws Exception {
        this.mockMvc.perform(
            post("/user/demo"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("name", is("User Demo")))
            .andExpect(jsonPath("email", is("userdemo.0@gmail.com")));

        this.mockMvc.perform(
            post("/user/demo"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("name", is("User Demo")))
            .andExpect(jsonPath("email", is("userdemo.1@gmail.com")));

        this.mockMvc.perform(
            post("/user/demo"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("name", is("User Demo")))
            .andExpect(jsonPath("email", is("userdemo.2@gmail.com")));

        this.userRepository.deleteAll();
        this.emailNumberRepository.deleteAll();
    }
}
