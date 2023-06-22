package com.api.note.integration.services.note;

import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.api.note.dtos.note.NoteDTOFindByTitleRequest;
import com.api.note.dtos.note.NoteDTOFindByTitleResponse;
import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.note.NoteService;
import com.api.note.services.util.BadRequestException;
import com.api.note.utils.builders.note.NoteDTOFindByTitleRequestBuilder;
import com.api.note.utils.builders.note.NoteModelBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;

@SpringBootTest
public class NoteServiceFindByTitleTests {
    @Autowired
    private NoteService noteService;    
    
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void esperoQueBusqueAsNotasPelosTitulos() throws BadRequestException {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        NoteModel noteModelWithFooBarOnTitle = NoteModelBuilder.createWithValidData();
        noteModelWithFooBarOnTitle.userModel = userModel;

        this.noteRepository.save(noteModelWithFooBarOnTitle);

        NoteModel noteModelWithBarFooOnTitle = NoteModelBuilder.createWithValidData();
        noteModelWithBarFooOnTitle.userModel = userModel;

        noteModelWithBarFooOnTitle.content = "Bar foo";

        this.noteRepository.save(noteModelWithBarFooOnTitle);

        // Teste principal
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = NoteDTOFindByTitleRequestBuilder.createWithValidData();

        noteDTOFindByTitleRequest.userId = saveUserModelResponse.userId.toString();
        
        List<NoteDTOFindByTitleResponse> noteDTOFindByTitleResponseList = this.noteService.findByTitle(noteDTOFindByTitleRequest);

        Assertions.assertThat(noteDTOFindByTitleResponseList.get(0).title).isEqualTo("Foo Bar");
        Assertions.assertThat(noteDTOFindByTitleResponseList.get(0).content).isEqualTo("Foo bar");

        Assertions.assertThat(noteDTOFindByTitleResponseList.get(1).title).isEqualTo("Foo Bar");
        Assertions.assertThat(noteDTOFindByTitleResponseList.get(1).content).isEqualTo("Bar foo");

        // Limpeza de dados de ambiente
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontrado() throws BadRequestException {
        // Teste principal
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = NoteDTOFindByTitleRequestBuilder.createWithValidData();

        noteDTOFindByTitleRequest.userId = UUID.randomUUID().toString();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.findByTitle(noteDTOFindByTitleRequest))
            .withMessage("Error: usuário não encontrado");
    }

    @Test
    public void esperoQueRetorneUmErroDeNotaNaoEncontradaPeloTituloInformado() throws BadRequestException {
        // Preparo de dados de ambiente
        UserModel userModelForGetUserId = UserModelBuilder.createWithValidData();
        userModelForGetUserId.name = "Bar Foo";
        userModelForGetUserId.email = "barfoo@gmail.com";
        this.userRepository.save(userModelForGetUserId);

        UserModel userModel = UserModelBuilder.createWithValidData();
        this.userRepository.save(userModel);

        NoteModel noteModelWithFooBarOnTitle = NoteModelBuilder.createWithValidData();
        noteModelWithFooBarOnTitle.userModel = userModel;

        this.noteRepository.save(noteModelWithFooBarOnTitle);

        NoteModel noteModelWithBarFooOnTitle = NoteModelBuilder.createWithValidData();
        noteModelWithBarFooOnTitle.userModel = userModel;

        noteModelWithBarFooOnTitle.content = "Bar foo";

        this.noteRepository.save(noteModelWithBarFooOnTitle);

        // Teste principal
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = NoteDTOFindByTitleRequestBuilder.createWithValidData();

        noteDTOFindByTitleRequest.userId = userModelForGetUserId.userId.toString();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.findByTitle(noteDTOFindByTitleRequest))
            .withMessage("Error: nenhuma nota encontrada com este título");

        // Limpeza de dados de ambiente
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
