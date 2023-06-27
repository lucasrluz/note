package com.api.note.integration.controllers.note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.CoreMatchers.is;
import com.api.note.dtos.note.NoteDTOFindByTitleRequest;
import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.utils.builders.note.NoteDTOFindByTitleRequestBuilder;
import com.api.note.utils.builders.note.NoteModelBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerFindByTitleTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

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
    public void esperoQueRetorneUmCodigoDeStatus200ComOsDadosFornecidosNaBusca() throws Exception {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        NoteModel firstNoteModel = NoteModelBuilder.createWithValidData();
        firstNoteModel.userModel = userModel;
        this.noteRepository.save(firstNoteModel);

        NoteModel secondNoteModel = NoteModelBuilder.createWithValidData();
        secondNoteModel.userModel = userModel;
        this.noteRepository.save(secondNoteModel);

        // Teste principal
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = NoteDTOFindByTitleRequestBuilder.createWithValidData();

        String url = "/note/title/" + saveUserModelResponse.userId.toString();

        this.mockMvc.perform(
            get(url)
            .contentType("application/json")
            .content(asJsonString(noteDTOFindByTitleRequest)))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'title':'Foo Bar', 'content':'Foo bar'},{'title':'Foo Bar', 'content':'Foo bar'}]"));

        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus404ComUmaMensagemDeErroDeUsuarioNaoEncontrado() throws Exception {
        // Teste principal
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = NoteDTOFindByTitleRequestBuilder.createWithValidData();

        String url = "/note/title/" + UUID.randomUUID().toString();

        this.mockMvc.perform(
            get(url)
            .contentType("application/json")
            .content(asJsonString(noteDTOFindByTitleRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$", is("Error: usuário não encontrado")));
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus404ComUmMensagemDeErroDeNotaNaoEncontradaPeloTituloFornecido() throws Exception {
        // Preparo de dados de ambiente
        UserModel userModelForGetUserId = UserModelBuilder.createWithValidData();
        userModelForGetUserId.name = "Bar Foo";
        userModelForGetUserId.email = "barfoo@gmail.com";
        UserModel saveUserModelForGetUserIdResponse = this.userRepository.save(userModelForGetUserId);

        UserModel userModel = UserModelBuilder.createWithValidData();
        this.userRepository.save(userModel);

        NoteModel firstNoteModel = NoteModelBuilder.createWithValidData();
        firstNoteModel.userModel = userModel;
        this.noteRepository.save(firstNoteModel);

        NoteModel secondNoteModel = NoteModelBuilder.createWithValidData();
        secondNoteModel.userModel = userModel;
        this.noteRepository.save(secondNoteModel);

        // Teste principal
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = NoteDTOFindByTitleRequestBuilder.createWithValidData();

        String url = "/note/title/" + saveUserModelForGetUserIdResponse.userId.toString();

        this.mockMvc.perform(
            get(url)
            .contentType("application/json")
            .content(asJsonString(noteDTOFindByTitleRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$", is("Error: nenhuma nota encontrada com este título")));

        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
