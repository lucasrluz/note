package com.api.note.integration.controllers.note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
        
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        this.userRepository.save(userModel);

        NoteModel firstNoteModel = NoteModelBuilder.createWithValidData();
        firstNoteModel.userModel = userModel;
        
        NoteModel secondNoteModel = NoteModelBuilder.createWithValidData();
        secondNoteModel.userModel = userModel;

        NoteModel firstNoteModelSaveResponse = this.noteRepository.save(firstNoteModel);
        NoteModel secondNoteModelSaveResponse = this.noteRepository.save(secondNoteModel);
        
        String url = "/note";

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        String jsonResponse = 
            "[{'noteId':'" + firstNoteModelSaveResponse.noteId.toString() + "', 'title':'Foo Bar', 'content':'Foo bar'}," +
            "{'noteId':'" + secondNoteModelSaveResponse.noteId.toString() + "', 'title':'Foo Bar', 'content':'Foo bar'}]";

        this.mockMvc.perform(
            get(url)
            .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andExpect(content().json(jsonResponse));
    
		this.noteRepository.deleteAll();
		this.userRepository.deleteAll();
	}

	@Test
    public void esperoQueRetorneUmCodigoDeStatus200ComUmaListaVazia() throws Exception {
        this.userRepository.deleteAll();
        
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        this.userRepository.save(userModel);
        
        String url = "/note";

        // JWT
        String jwt = this.jwtService.generateJwt(saveUserModelResponse.userId.toString());

        this.mockMvc.perform(
            get(url)
            .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    
		this.userRepository.deleteAll();
	}
}
