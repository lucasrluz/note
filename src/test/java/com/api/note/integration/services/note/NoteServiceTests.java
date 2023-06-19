package com.api.note.integration.services.note;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.api.note.domains.note.exceptions.InvalidNoteDomainException;
import com.api.note.dtos.note.NoteDTOSaveRequest;
import com.api.note.dtos.note.NoteDTOSaveResponse;
import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.note.NoteService;
import com.api.note.services.util.BadRequestException;
import com.api.note.utils.builders.note.NoteDTOSaveRequestBuilder;
import com.api.note.utils.builders.note.NoteModelBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;

@SpringBootTest
public class NoteServiceTests {
    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void esperoQueSalveUmaNovaNotaNoSistema() throws InvalidNoteDomainException, BadRequestException {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();
        noteDTOSaveRequest.userId = saveUserModelResponse.userId.toString();

        NoteDTOSaveResponse noteDTOSaveResponse = this.noteService.save(noteDTOSaveRequest);

        Assertions.assertThat(noteDTOSaveResponse.title).isEqualTo("Foo Bar");

        // Teste nos dados do banco de dados
        NoteModel noteModel = this.noteRepository.findById(
            UUID.fromString(noteDTOSaveResponse.noteId)
        ).get();

        Assertions.assertThat(noteModel.content).isEqualTo("Foo bar");
        Assertions.assertThat(noteModel.userModel.userId).isEqualTo(userModel.userId);

        // Limpeza dos dados de ambiente
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmErroDeTituloInvalidoDadoUmValorVazio() {
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithEmptyTitle();
        
        Assertions.assertThatExceptionOfType(InvalidNoteDomainException.class)
            .isThrownBy(() -> this.noteService.save(noteDTOSaveRequest))
            .withMessage("title: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmErroDeConteudoInvalidoDadoUmValorVazio() {
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithEmptyContent();
        
        Assertions.assertThatExceptionOfType(InvalidNoteDomainException.class)
            .isThrownBy(() -> this.noteService.save(noteDTOSaveRequest))
            .withMessage("content: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontradoDadoUmIdInvalido() {
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();
        noteDTOSaveRequest.userId = UUID.randomUUID().toString();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.save(noteDTOSaveRequest))
            .withMessage("Error: usuário não encontrado");
    }

    @Test
    public void esperoQueRetorneUmErroDeNotaJaCriadaDadoUmTituloJaUsado() {
        // Preparo de dados de ambiente
        UserModel userModel = UserModelBuilder.createWithValidData();
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        NoteModel noteModel = NoteModelBuilder.createWithValidData();
        noteModel.userModel = saveUserModelResponse;

        this.noteRepository.save(noteModel);

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();
        noteDTOSaveRequest.userId = saveUserModelResponse.userId.toString();
        
        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.save(noteDTOSaveRequest))
            .withMessage("Error: Já existe uma nota criada com este título");
        
        // Limpeza dos dados de ambiente
        this.noteRepository.deleteAll();
        this.userRepository.deleteAll();
    }
    
}
