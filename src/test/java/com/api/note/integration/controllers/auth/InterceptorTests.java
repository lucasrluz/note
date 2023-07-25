package com.api.note.integration.controllers.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import com.api.note.dtos.note.NoteDTOSaveRequest;
import com.api.note.models.UserModel;
import com.api.note.repositories.UserRepository;
import com.api.note.services.auth.JwtService;
import com.api.note.utils.builders.note.NoteDTOSaveRequestBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class InterceptorTests {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;

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
    public void esperoQueARequisicaoSejaInterceptadaERetorneUmErroDeJwtObrigatorio() throws Exception {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();
        
        String url = "/note/" + saveUserModelResponse.userId.toString();

        this.mockMvc.perform(
            post(url)
            .contentType("application/json")
            .content(asJsonString(noteDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("JWT is required")));

        // Limpeza de dados do ambiente
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueARequisicaoSejaInterceptadaERetorneUmErroDeUserIdObrigatorio() throws Exception {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // JWT
        String jwt = this.jwtService.generateJwt(userModel.email);

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();
        
        String url = "/note/" + saveUserModelResponse.userId.toString();

        this.mockMvc.perform(
            post(url)
            .header("JWT", jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("UserId is required")));

        // Limpeza de dados do ambiente
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueARequisicaoSejaInterceptadaERetorneUmErroDeJWTInvalido() throws Exception {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // JWT
        String jwt = this.jwtService.generateJwt("barfoo@gmail.com");

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();
        
        String url = "/note/" + saveUserModelResponse.userId.toString();

        this.mockMvc.perform(
            post(url)
            .header("JWT", jwt)
            .header("UserId", saveUserModelResponse.userId.toString())
            .contentType("application/json")
            .content(asJsonString(noteDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("Expected sub claim to be: foobar@gmail.com, but was: barfoo@gmail.com.")));

        // Limpeza de dados do ambiente
        this.userRepository.deleteAll();
    }
}
