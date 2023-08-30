package com.api.note.integration.controllers.note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.is;
import com.api.note.dtos.note.NoteDTOUpdateRequest;
import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.auth.JwtService;
import com.api.note.utils.builders.note.NoteDTOUpdateRequestBuilder;
import com.api.note.utils.builders.note.NoteModelBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerUpdateTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

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
    public void esperoQueOTituloEOConteudoDaNotaSejaAlterado() throws Exception {
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();

        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        NoteModel noteModel = NoteModelBuilder.createWithValidData();
        noteModel.userModel = userModel;
        NoteModel saveNoteModelResponse = this.noteRepository.save(noteModel);

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        // Teste principal
        NoteDTOUpdateRequest noteDTOUpdateRequest = NoteDTOUpdateRequestBuilder.createWithValidData();
        noteDTOUpdateRequest.title = "Bar Foo";
        noteDTOUpdateRequest.content = "Bar foo";
        noteDTOUpdateRequest.noteId = saveNoteModelResponse.noteId.toString();

        this.mockMvc.perform(
            put("/note")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOUpdateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("noteId", is(saveNoteModelResponse.noteId.toString())))
            .andExpect(jsonPath("title", is(noteDTOUpdateRequest.title)))
            .andExpect(jsonPath("content", is(noteDTOUpdateRequest.content))
        );

        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmErroDeTituloInvalidoDadoUmValorVazio() throws Exception {
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();

        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        NoteModel noteModel = NoteModelBuilder.createWithValidData();
        noteModel.userModel = userModel;
        NoteModel saveNoteModelResponse = this.noteRepository.save(noteModel);

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        // Teste principal
        NoteDTOUpdateRequest noteDTOUpdateRequest = NoteDTOUpdateRequestBuilder.createWithEmptyTitle();
        noteDTOUpdateRequest.noteId = saveNoteModelResponse.noteId.toString();

        this.mockMvc.perform(
            put("/note")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOUpdateRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("title: must not be blank"))
        );

        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmErroDeConteudoInvalidoDadoUmValorVazio() throws Exception {
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();

        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        NoteModel noteModel = NoteModelBuilder.createWithValidData();
        noteModel.userModel = userModel;
        NoteModel saveNoteModelResponse = this.noteRepository.save(noteModel);

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        // Teste principal
        NoteDTOUpdateRequest noteDTOUpdateRequest = NoteDTOUpdateRequestBuilder.createWithEmptyContent();
        noteDTOUpdateRequest.noteId = saveNoteModelResponse.noteId.toString();

        this.mockMvc.perform(
            put("/note")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOUpdateRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("content: must not be blank"))
        );

        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmErroDeNotaNaoEncontrada() throws Exception {
        this.userRepository.deleteAll();

        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        // Teste principal
        NoteDTOUpdateRequest noteDTOUpdateRequest = NoteDTOUpdateRequestBuilder.createWithValidData();
        noteDTOUpdateRequest.title = "Bar Foo";
        noteDTOUpdateRequest.content = "Bar foo";
        noteDTOUpdateRequest.noteId = UUID.randomUUID().toString();

        this.mockMvc.perform(
            put("/note")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(noteDTOUpdateRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$", is("Error: nota não encontrada"))
        );

        this.userRepository.deleteAll();
    }
}
