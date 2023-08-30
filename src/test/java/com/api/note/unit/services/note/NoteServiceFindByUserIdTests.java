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

import com.api.note.dtos.note.NoteDTOFindByUserIdRequest;
import com.api.note.dtos.note.NoteDTOFindByUserIdResponse;
import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.note.NoteService;
import com.api.note.services.util.BadRequestException;
import com.api.note.utils.builders.note.NoteDTOFindByUserIdRequestBuilder;
import com.api.note.utils.builders.note.NoteModelBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class NoteServiceFindByUserIdTests {
    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void esperoQueBusqueAsNotasPeloIdDoUsuario() throws BadRequestException {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        NoteModel firstNoteModel = NoteModelBuilder.createWithValidData();
        firstNoteModel.noteId = UUID.randomUUID();
        firstNoteModel.userModel = userModelMock;

        NoteModel secondNoteModel = NoteModelBuilder.createWithValidData();
        secondNoteModel.noteId = UUID.randomUUID();
        secondNoteModel.userModel = userModelMock;
        secondNoteModel.content = "Bar foo";

        List<NoteModel> noteModelList = new ArrayList<NoteModel>();

        noteModelList.add(firstNoteModel);
        noteModelList.add(secondNoteModel);

        BDDMockito.when(this.noteRepository.findByUserModel(ArgumentMatchers.any())).thenReturn(noteModelList);

        // Teste principal
        NoteDTOFindByUserIdRequest noteDTOFindByUserIdRequest = NoteDTOFindByUserIdRequestBuilder.createWithValidData();

        List<NoteDTOFindByUserIdResponse> noteDTOFindByUserIdResponse = this.noteService.findByUserId(noteDTOFindByUserIdRequest);

        Assertions.assertThat(noteDTOFindByUserIdResponse.get(0).noteId).isEqualTo(firstNoteModel.noteId.toString());
        Assertions.assertThat(noteDTOFindByUserIdResponse.get(0).title).isEqualTo(firstNoteModel.title);
        Assertions.assertThat(noteDTOFindByUserIdResponse.get(0).content).isEqualTo(firstNoteModel.content);

        Assertions.assertThat(noteDTOFindByUserIdResponse.get(1).noteId).isEqualTo(secondNoteModel.noteId.toString());
        Assertions.assertThat(noteDTOFindByUserIdResponse.get(1).title).isEqualTo(secondNoteModel.title);
        Assertions.assertThat(noteDTOFindByUserIdResponse.get(1).content).isEqualTo(secondNoteModel.content);
    }

    @Test
    public void esperoQueRetorneUmErroDeNenhumaNotaEncontrada() throws BadRequestException {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        List<NoteModel> noteModelList = new ArrayList<NoteModel>();
        BDDMockito.when(this.noteRepository.findByUserModel(ArgumentMatchers.any())).thenReturn(noteModelList);

        // Teste principal
        NoteDTOFindByUserIdRequest noteDTOFindByUserIdRequest = NoteDTOFindByUserIdRequestBuilder.createWithValidData();
        
        List<NoteDTOFindByUserIdResponse> findByUserIdResponse = this.noteService.findByUserId(noteDTOFindByUserIdRequest);
        
        Assertions.assertThat(findByUserIdResponse.isEmpty()).isEqualTo(true);
    }
}
