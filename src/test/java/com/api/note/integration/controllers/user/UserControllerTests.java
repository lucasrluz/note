package com.api.note.integration.controllers.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.api.note.dtos.user.UserDTOSaveRequest;
import com.api.note.models.UserModel;
import com.api.note.repositories.UserRepository;
import com.api.note.utils.builders.user.UserDTOSaveRequestBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    public static String asJsonString(final Object obj) {
        try {
          return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus201ComOsDadosSalvosNoSistema() throws Exception {
        this.userRepository.deleteAll();

        UserDTOSaveRequest userDTOSaveRequest = UserDTOSaveRequestBuilder.createWithValidData();

        this.mockMvc.perform(
            post("/user")
            .contentType("application/json")
            .content(asJsonString(userDTOSaveRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("name", is("Foo Bar")))
            .andExpect(jsonPath("email", is("foobar@gmail.com")));

        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaMessagemDeErroDeNomeInvalidoDadoUmNomeVazio() throws Exception {
        UserDTOSaveRequest userDTOSaveRequest = UserDTOSaveRequestBuilder.createWithEmptyName();

        this.mockMvc.perform(
            post("/user")
            .contentType("application/json")
            .content(asJsonString(userDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("name: must not be blank")));

    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaMessagemDeErroDeEmailInvalidoDadoUmEmailVazio() throws Exception {
        UserDTOSaveRequest userDTOSaveRequest = UserDTOSaveRequestBuilder.createWithEmptyEmail();

        this.mockMvc.perform(
            post("/user")
            .contentType("application/json")
            .content(asJsonString(userDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("email: must not be blank")));

    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaMessagemDeErroDeEmailInvalidoDadoUmFormatoDeEmailInvalido() throws Exception {
        UserDTOSaveRequest userDTOSaveRequest = UserDTOSaveRequestBuilder.createWithInvalidEmailFormat();

        this.mockMvc.perform(
            post("/user")
            .contentType("application/json")
            .content(asJsonString(userDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("email: must be a well-formed email address")));

    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaMessagemDeErroDeSenhaInvalidaDadaUmaSenhaVazia() throws Exception {
        UserDTOSaveRequest userDTOSaveRequest = UserDTOSaveRequestBuilder.createWithEmptyPassword();

        this.mockMvc.perform(
            post("/user")
            .contentType("application/json")
            .content(asJsonString(userDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("password: must not be blank")));

    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaMessagemDeEmailJaCadastrado() throws Exception {
        this.userRepository.deleteAll();
        
        UserModel userModel = UserModelBuilder.createWithValidData();
        
        this.userRepository.save(userModel);

        UserDTOSaveRequest userDTOSaveRequest = UserDTOSaveRequestBuilder.createWithValidData();

        this.mockMvc.perform(
            post("/user")
            .contentType("application/json")
            .content(asJsonString(userDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("Error: Este e-mail j\u00E1 est\u00E1 cadastrado no sistema")));

        this.userRepository.deleteAll();
    }
}
