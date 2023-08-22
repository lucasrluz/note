package com.api.note.unit.services.note;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.note.dtos.note.NoteDTODeleteRequest;
import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.note.NoteService;
import com.api.note.services.util.BadRequestException;
import com.api.note.utils.builders.note.NoteModelBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class NoteServiceDeleteTests {
    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void esperoQueExcluaANota() throws BadRequestException {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        NoteModel noteModelMock = NoteModelBuilder.createWithValidData();
        noteModelMock.noteId = UUID.randomUUID();
        noteModelMock.userModel = userModelMock;
        Optional<NoteModel> noteModeOptionalMock = Optional.of(noteModelMock);
        BDDMockito.when(this.noteRepository.findByNoteIdAndUserModel(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(noteModeOptionalMock);

        // Teste principal
        NoteDTODeleteRequest noteDTODeleteRequest = new NoteDTODeleteRequest(noteModelMock.noteId.toString(), userModelMock.userId.toString());

        Assertions.assertThat(this.noteService.delete(noteDTODeleteRequest).noteId).isEqualTo(noteDTODeleteRequest.noteId);
    }

    @Test
    public void esperoQuerRetorneUmErroDeUsuarioNaoContrado() throws BadRequestException {
        // Mocks
        Optional<UserModel> userModelOptionalMock = Optional.empty();
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        // Teste principal
        NoteDTODeleteRequest noteDTODeleteRequest = new NoteDTODeleteRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        
        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.delete(noteDTODeleteRequest))
            .withMessage("Error: usuário não encontrada");
        }

    @Test
    public void esperoQueRetorneUmErroDeNotaNaoEncontrada() throws BadRequestException {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        Optional<NoteModel> noteModeOptionalMock = Optional.empty();
        BDDMockito.when(this.noteRepository.findByNoteIdAndUserModel(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(noteModeOptionalMock);

        // Teste principal
        NoteDTODeleteRequest noteDTODeleteRequest = new NoteDTODeleteRequest(UUID.randomUUID().toString(), userModelMock.userId.toString());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.delete(noteDTODeleteRequest))
            .withMessage("Error: nota não encontrada");
    }
}
