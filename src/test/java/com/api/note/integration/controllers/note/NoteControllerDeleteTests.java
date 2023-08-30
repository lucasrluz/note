package com.api.note.integration.controllers.note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;

import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.auth.JwtService;
import com.api.note.utils.builders.note.NoteModelBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerDeleteTests {
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
    public void esperoQueRetorneUmCodigoDeStatus200EExcluaANotaDoUsuario() throws Exception {
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();

        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        NoteModel noteModel = NoteModelBuilder.createWithValidData();
        noteModel.userModel = userModel;
        NoteModel saveNoteModelResponse = this.noteRepository.save(noteModel);

        // Teste principal
        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        MockHttpServletResponse httpServletResponse = this.mockMvc.perform(
            delete("/note/" + saveNoteModelResponse.noteId.toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        Assertions.assertThat(httpServletResponse.getStatus()).isEqualTo(200);
        
        String noteId = new JSONObject(httpServletResponse.getContentAsString()).getString("noteId");
        Assertions.assertThat(noteId).isEqualTo(saveNoteModelResponse.noteId.toString());

        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }

	@Test
    public void esperoQueRetorneUmCodigoDeStatus404ComUmaMensagemDeErroDeNotaNaoEncontrada() throws Exception {
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();

        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // Teste principal
        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        MockHttpServletResponse httpServletResponse = this.mockMvc.perform(
            delete("/note/" + UUID.randomUUID().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        Assertions.assertThat(httpServletResponse.getStatus()).isEqualTo(404);
        Assertions.assertThat(httpServletResponse.getContentAsString()).isEqualTo("Error: nota não encontrada");

        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
