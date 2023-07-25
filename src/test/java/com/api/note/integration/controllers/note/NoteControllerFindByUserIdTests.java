package com.api.note.integration.controllers.note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.CoreMatchers.is;

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
public class NoteControllerFindByUserIdTests {
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
    public void esperoQueRetorneUmCodigoDeStatus201ComAsNotasDoUsuario() throws Exception {
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        this.userRepository.save(userModel);

        NoteModel firstNoteModel = NoteModelBuilder.createWithValidData();
        firstNoteModel.userModel = userModel;
        
        NoteModel secondNoteModel = NoteModelBuilder.createWithValidData();
        secondNoteModel.userModel = userModel;

        this.noteRepository.save(firstNoteModel);
        this.noteRepository.save(secondNoteModel);
        
        String url = "/note/" + saveUserModelResponse.userId.toString();

        // JWT
        String jwt = this.jwtService.generateJwt(userModel.email);

        this.mockMvc.perform(
            get(url)
            .header("JWT", jwt)
            .header("UserId", saveUserModelResponse.userId.toString()))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'title':'Foo Bar', 'content':'Foo bar'},{'title':'Foo Bar', 'content':'Foo bar'}]"));
    
		this.noteRepository.deleteAll();
		this.userRepository.deleteAll();
	}

	@Test
    public void esperoQueRetorneUmCodigoDeStatus404ComUmaMensagemDeUsuaioNaoEncontrado() throws Exception {        
        String userId = UUID.randomUUID().toString();
        
        String url = "/note/" + userId;

        // JWT
        String jwt = this.jwtService.generateJwt("foobar@gmail.com");

        this.mockMvc.perform(
            get(url)
            .header("JWT", jwt)
            .header("UserId", userId))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("Error: usuário não encontrado")));
	}

	@Test
    public void esperoQueRetorneUmCodigoDeStatus404ComUmaMensagemDeNotasNaoEncontradas() throws Exception {
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        this.userRepository.save(userModel);

        String userId = saveUserModelResponse.userId.toString();
        
        String url = "/note/" + userId;

        // JWT
        String jwt = this.jwtService.generateJwt("foobar@gmail.com");

        this.mockMvc.perform(
            get(url)
            .header("JWT", jwt)
            .header("UserId", userId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$", is("Error: Este usuário não possui notas criadas")));
    
		this.userRepository.deleteAll();
	}
}
