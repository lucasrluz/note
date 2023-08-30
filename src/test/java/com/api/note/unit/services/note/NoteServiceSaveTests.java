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

@ExtendWith(SpringExtension.class)
public class NoteServiceSaveTests {
    @InjectMocks
    private NoteService noteService;
    
    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void esperoQueSalveUmaNovaNotaNoSistema() throws InvalidNoteDomainException, BadRequestException {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        Optional<UserModel> userModelOptional = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptional);

        NoteModel noteModelMock = NoteModelBuilder.createWithValidData();
        noteModelMock.noteId = UUID.randomUUID();
        noteModelMock.userModel = userModelMock;
        BDDMockito.when(this.noteRepository.save(ArgumentMatchers.any())).thenReturn(noteModelMock);

        // Teste principal
        NoteDTOSaveRequest noteDTOSaveRequest = NoteDTOSaveRequestBuilder.createWithValidData();
        noteDTOSaveRequest.userId = userModelMock.userId.toString();
        NoteDTOSaveResponse noteDTOSaveResponse = this.noteService.save(noteDTOSaveRequest);

        Assertions.assertThat(noteDTOSaveResponse.noteId).isEqualTo(noteModelMock.noteId.toString());
        Assertions.assertThat(noteDTOSaveResponse.title).isEqualTo(noteModelMock.title);
        Assertions.assertThat(noteDTOSaveResponse.content).isEqualTo(noteModelMock.content);
    }
}
