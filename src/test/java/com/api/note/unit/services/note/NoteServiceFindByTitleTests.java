package com.api.note.unit.services.note;

import java.util.ArrayList;
import java.util.List;
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

@ExtendWith(SpringExtension.class)
public class NoteServiceFindByTitleTests {
    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void esperoQueRetorneAsNotasPeloTituloInformado() throws BadRequestException {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        NoteModel firstNoteModelMock = NoteModelBuilder.createWithValidData();
        firstNoteModelMock.userModel = userModelMock;

        NoteModel secondNoteModelMock = NoteModelBuilder.createWithValidData();
        secondNoteModelMock.content = "Bar foo";
        secondNoteModelMock.userModel = userModelMock;

        List<NoteModel> noteModelListMock = new ArrayList<NoteModel>();

        noteModelListMock.add(firstNoteModelMock);
        noteModelListMock.add(secondNoteModelMock);

        BDDMockito.when(this.noteRepository.findByTitleAndUserModel(
            ArgumentMatchers.any(),
            ArgumentMatchers.any()
        )).thenReturn(noteModelListMock);

        // Teste principal
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = NoteDTOFindByTitleRequestBuilder.createWithValidData();
        noteDTOFindByTitleRequest.userId = userModelMock.userId.toString();

        List<NoteDTOFindByTitleResponse> noteDTOFindByTitleResponseList = this.noteService.findByTitle(noteDTOFindByTitleRequest);
        
        Assertions.assertThat(noteDTOFindByTitleResponseList.get(0).title).isEqualTo(firstNoteModelMock.title);
        Assertions.assertThat(noteDTOFindByTitleResponseList.get(0).content).isEqualTo(firstNoteModelMock.content);
        
        Assertions.assertThat(noteDTOFindByTitleResponseList.get(1).title).isEqualTo(secondNoteModelMock.title);
        Assertions.assertThat(noteDTOFindByTitleResponseList.get(1).content).isEqualTo(secondNoteModelMock.content);
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontradoDadoUmUserIdInvalido() throws BadRequestException {
        // Mocks
        Optional<UserModel> userModelOptionalMock = Optional.empty();
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);
        
        // Teste principal
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = NoteDTOFindByTitleRequestBuilder.createWithValidData();
        noteDTOFindByTitleRequest.userId = UUID.randomUUID().toString();
        
        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.findByTitle(noteDTOFindByTitleRequest))
            .withMessage("Error: usuário não encontrado");

    }

    @Test
    public void esperoQueRetorneUmErroDeNotaNaoEncontradaPeloTituloInformado() throws BadRequestException {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        List<NoteModel> noteModelListMock = new ArrayList<NoteModel>();

        BDDMockito.when(this.noteRepository.findByTitleAndUserModel(
            ArgumentMatchers.any(),
            ArgumentMatchers.any()
        )).thenReturn(noteModelListMock);

        // Teste principal
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = NoteDTOFindByTitleRequestBuilder.createWithValidData();
        noteDTOFindByTitleRequest.userId = userModelMock.userId.toString();
        
        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.noteService.findByTitle(noteDTOFindByTitleRequest))
            .withMessage("Error: nenhuma nota encontrada com este título");
    }
}
