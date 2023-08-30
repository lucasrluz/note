package com.api.note.integration.controllers.auth;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.api.note.dtos.note.NoteDTOSaveRequest;
import com.api.note.services.auth.JwtService;
import com.api.note.utils.builders.note.NoteDTOSaveRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.is;
@SpringBootTest
@AutoConfigureMockMvc
public class AuthFilterTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    public static String asJsonString(final Object obj) {
        try {
          return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus403ComUmaMessagemDeJwtInvalido() throws Exception {
        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();

        // JWT
        String jwt = this.jwtService.generateJwt(UUID.randomUUID().toString());
        
        String url = "/note";

        this.mockMvc.perform(
            post(url)
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOSaveRequest)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$", is("Error: invalid jwt")));
    }
}
