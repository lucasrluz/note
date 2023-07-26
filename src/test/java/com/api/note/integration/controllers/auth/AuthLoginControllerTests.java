package com.api.note.integration.controllers.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.assertj.core.api.Assertions;

import com.api.note.dtos.auth.LoginDTORequest;
import com.api.note.models.UserModel;
import com.api.note.repositories.UserRepository;
import com.api.note.services.auth.JwtService;
import com.api.note.utils.builders.auth.LoginDTORequestBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthLoginControllerTests {
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
    public void esperoQueRetorneUmJwtValido() throws Exception {        
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        LoginDTORequest loginDTORequest = LoginDTORequestBuilder.createWithValidData();

        MvcResult mvcResult = this.mockMvc.perform(
            post("/auth/login")
            .contentType("application/json")
            .content(asJsonString(loginDTORequest)))
            .andReturn();

        String jwt = new JSONObject(mvcResult.getResponse().getContentAsString()).getString("jwt");

        Assertions.assertThat(this.jwtService.validateJwt(jwt)).isEqualTo(saveUserModelResponse.userId.toString());    
        Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);

        this.userRepository.deleteAll();
    }

	@Test
    public void esperoQueRetorneUmErroDadoUmEmailInvalido() throws Exception {        
        LoginDTORequest loginDTORequest = LoginDTORequestBuilder.createWithValidData();

        MvcResult mvcResult = this.mockMvc.perform(
            post("/auth/login")
            .contentType("application/json")
            .content(asJsonString(loginDTORequest)))
            .andReturn();

		Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Error: E-mail ou senha inválido");
        Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

	@Test
    public void esperoQueRetorneUmErroDadoUmaSenhaInvalida() throws Exception {        
        UserModel userModel = UserModelBuilder.createWithValidData();
        this.userRepository.save(userModel);

        LoginDTORequest loginDTORequest = LoginDTORequestBuilder.createWithValidData();
		loginDTORequest.password = "456";

        MvcResult mvcResult = this.mockMvc.perform(
            post("/auth/login")
            .contentType("application/json")
            .content(asJsonString(loginDTORequest)))
            .andReturn();

		Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Error: E-mail ou senha inválido");
        Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);

        this.userRepository.deleteAll();
    }
}
