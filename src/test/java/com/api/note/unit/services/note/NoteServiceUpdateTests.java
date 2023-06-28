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

import com.api.note.domains.note.exceptions.InvalidNoteDomainException;
import com.api.note.dtos.note.NoteDTOUpdateRequest;
import com.api.note.dtos.note.NoteDTOUpdateResponse;
import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.note.NoteService;
import com.api.note.services.util.BadRequestException;
import com.api.note.utils.builders.note.NoteDTOUpdateRequestBuilder;
import com.api.note.utils.builders.note.NoteModelBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class NoteServiceUpdateTests {
    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void esperoQueEditeOsDadosDaNota() throws InvalidNoteDomainException, BadRequestException {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        Optional<UserModel> userModelOptional = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptional);

        NoteModel noteModelMockForFindByNoteIdMethod = NoteModelBuilder.createWithValidData();
        noteModelMockForFindByNoteIdMethod.userModel = userModelMock;
        Optional<NoteModel> noteModelOptionalMock = Optional.of(noteModelMockForFindByNoteIdMethod);
        BDDMockito.when(this.noteRepository.findById(ArgumentMatchers.any())).thenReturn(noteModelOptionalMock);

        NoteModel noteModelMockForSaveMethod = NoteModelBuilder.createWithValidData();
        noteModelMockForSaveMethod.title = "Bar Foo";
        noteModelMockForSaveMethod.content = "Bar foo";
        noteModelMockForSaveMethod.noteId = UUID.randomUUID();
        noteModelMockForSaveMethod.userModel = userModelMock;
        BDDMockito.when(this.noteRepository.save(ArgumentMatchers.any())).thenReturn(noteModelMockForSaveMethod);

        // Teste principal
        NoteDTOUpdateRequest noteDTOUpdateRequest = NoteDTOUpdateRequestBuilder.createWithValidData();

        noteDTOUpdateRequest.userId = userModelMock.userId.toString();
        noteDTOUpdateRequest.noteId = noteModelMockForSaveMethod.noteId.toString();

        NoteDTOUpdateResponse noteDTOUpdateResponse = this.noteService.update(noteDTOUpdateRequest);

        Assertions.assertThat(noteDTOUpdateResponse.noteId).isEqualTo(noteModelMockForSaveMethod.noteId.toString());
        Assertions.assertThat(noteDTOUpdateResponse.title).isEqualTo(noteModelMockForSaveMethod.title);
        Assertions.assertThat(noteDTOUpdateResponse.content).isEqualTo(noteModelMockForSaveMethod.content);
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontrado() throws InvalidNoteDomainException, BadRequestException {
        // Mocks
        Optional<UserModel> userModelOptional = Optional.empty();
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptional);

        // Teste principal
        NoteDTOUpdateRequest noteDTOUpdateRequest = NoteDTOUpdateRequestBuilder.createWithValidData();

        noteDTOUpdateRequest.userId = UUID.randomUUID().toString();
        noteDTOUpdateRequest.noteId = UUID.randomUUID().toString();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.update(noteDTOUpdateRequest))
            .withMessage("Error: usuário não encontrado");
    }

    @Test
    public void esperoQueRetorneUmErroDeNotaNaoEncontrada() throws InvalidNoteDomainException, BadRequestException {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        Optional<UserModel> userModelOptional = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptional);

        Optional<NoteModel> noteModelOptionalMock = Optional.empty();
        BDDMockito.when(this.noteRepository.findById(ArgumentMatchers.any())).thenReturn(noteModelOptionalMock);

        // Teste principal
        NoteDTOUpdateRequest noteDTOUpdateRequest = NoteDTOUpdateRequestBuilder.createWithValidData();

        noteDTOUpdateRequest.userId = userModelMock.userId.toString();
        noteDTOUpdateRequest.noteId = UUID.randomUUID().toString();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.update(noteDTOUpdateRequest))
            .withMessage("Error: nota não encontrada");
    }
}
