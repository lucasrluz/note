package com.api.note.integration.controllers.note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.is;
import com.api.note.dtos.note.NoteDTOSaveRequest;
import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.auth.JwtService;
import com.api.note.utils.builders.note.NoteDTOSaveRequestBuilder;
import com.api.note.utils.builders.note.NoteModelBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

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
    public void esperoQueRetorneUmCodigoDeStatus201ComOsDadosSalvosNoSistema() throws Exception {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();
        
        String url = "/note";

        this.mockMvc.perform(
            post(url)
            .header("JWT", jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOSaveRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("title", is("Foo Bar")));

        // Limpeza de dados do ambiente
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus201ComOsDadosSalvosNoSistemaDadoDuasNotasComOMesmoTitulo() throws Exception {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        NoteModel noteModel = NoteModelBuilder.createWithValidData();
        noteModel.userModel = userModel;
        this.noteRepository.save(noteModel);

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();
        
        String url = "/note";

        this.mockMvc.perform(
            post(url)
            .header("JWT", jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOSaveRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("title", is("Foo Bar")));

        // Limpeza de dados do ambiente
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaMessagemDeErroDeTituloInvalidoDadoUmValorVazio() throws Exception {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithEmptyTitle();
        
        String url = "/note";

        this.mockMvc.perform(
            post(url)
            .header("JWT", jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("title: must not be blank")));

        // Limpeza de dados do ambiente
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaMessagemDeErroDeConteudoInvalidoDadoUmValorVazio() throws Exception {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithEmptyContent();
        
        String url = "/note";

        this.mockMvc.perform(
            post(url)
            .header("JWT", jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOSaveRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("content: must not be blank")));

        // Limpeza de dados do ambiente
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus404ComUmaMessagemDeErroDeUsuarioNaoEncontrado() throws Exception {
        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();

        // JWT
        String jwt = this.jwtService.generateJwt(UUID.randomUUID().toString());
        
        String url = "/note";

        this.mockMvc.perform(
            post(url)
            .header("JWT", jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOSaveRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$", is("Error: usuário não encontrado")));
    }
}
