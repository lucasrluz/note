package com.api.note.services.note;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.note.domains.note.NoteDomain;
import com.api.note.domains.note.exceptions.InvalidNoteDomainException;
import com.api.note.dtos.note.NoteDTOFindByTitleRequest;
import com.api.note.dtos.note.NoteDTOFindByTitleResponse;
import com.api.note.dtos.note.NoteDTOFindByUserIdRequest;
import com.api.note.dtos.note.NoteDTOFindByUserIdResponse;
import com.api.note.dtos.note.NoteDTOSaveRequest;
import com.api.note.dtos.note.NoteDTOSaveResponse;
import com.api.note.dtos.note.NoteDTOUpdateRequest;
import com.api.note.dtos.note.NoteDTOUpdateResponse;
import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.util.BadRequestException;

@Service
public class NoteService {
    private NoteRepository noteRepository;
    private UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public NoteDTOSaveResponse save(NoteDTOSaveRequest noteDTOSaveRequest) throws InvalidNoteDomainException, BadRequestException {
        NoteDomain noteDomain = NoteDomain.validate(
            noteDTOSaveRequest.title,
            noteDTOSaveRequest.content
        );

        Optional<UserModel> findUserByUserIdResponse = this.userRepository.findById(
            UUID.fromString(noteDTOSaveRequest.userId)
        );

        if (findUserByUserIdResponse.isEmpty()) {
            throw new BadRequestException("Error: usuário não encontrado");
        }

        NoteModel noteModel = new NoteModel(
            noteDomain.getTitle(),
            noteDomain.getContent(),
            findUserByUserIdResponse.get()
        );

        NoteModel saveNoteModelResponse = this.noteRepository.save(noteModel);

        return new NoteDTOSaveResponse(
            saveNoteModelResponse.noteId.toString(),
            saveNoteModelResponse.title
        );
    }

    public List<NoteDTOFindByUserIdResponse> findByUserId(NoteDTOFindByUserIdRequest noteDTOFindByUserIdRequest) throws BadRequestException {
        Optional<UserModel> findUserModelByUserIdResponse = this.userRepository.findById(
            UUID.fromString(noteDTOFindByUserIdRequest.userId)
        );

        if (findUserModelByUserIdResponse.isEmpty()) {
            throw new BadRequestException("Error: usuário não encontrado");
        }

        List<NoteModel> findNotesByUserIdResponse = this.noteRepository.findByUserModel(
            findUserModelByUserIdResponse.get()
        );

        List<NoteDTOFindByUserIdResponse> noteDTOFindByUserIdResponseList = new ArrayList<NoteDTOFindByUserIdResponse>();

        for (NoteModel noteModel : findNotesByUserIdResponse) {
            NoteDTOFindByUserIdResponse noteDTOFindByTitleResponse = new NoteDTOFindByUserIdResponse(
                noteModel.noteId.toString(),
                noteModel.title,
                noteModel.content
            );

            noteDTOFindByUserIdResponseList.add(noteDTOFindByTitleResponse);
        }

        return noteDTOFindByUserIdResponseList;
    }

    public List<NoteDTOFindByTitleResponse> findByTitle(NoteDTOFindByTitleRequest noteDTOFindByTitleRequest) throws BadRequestException {
        Optional<UserModel> findUserModelByUserIdResponse = this.userRepository.findById(
            UUID.fromString(noteDTOFindByTitleRequest.userId)
        );

        if (findUserModelByUserIdResponse.isEmpty()) {
            throw new BadRequestException("Error: usuário não encontrado");
        }

        List<NoteModel> findNotesByTitleResponse = this.noteRepository.findByTitleAndUserModel(
            noteDTOFindByTitleRequest.title,
            findUserModelByUserIdResponse.get()
        );

        if (findNotesByTitleResponse.isEmpty()) {
            throw new BadRequestException("Error: nenhuma nota encontrada com este título");
        }

        List<NoteDTOFindByTitleResponse> noteDTOFindByTitleResponseList = new ArrayList<NoteDTOFindByTitleResponse>();

        for (NoteModel noteModel : findNotesByTitleResponse) {
            NoteDTOFindByTitleResponse noteDTOFindByTitleResponse = new NoteDTOFindByTitleResponse(
                noteModel.title,
                noteModel.content
            );

            noteDTOFindByTitleResponseList.add(noteDTOFindByTitleResponse);
        }

        return noteDTOFindByTitleResponseList;
    }

    public NoteDTOUpdateResponse update(NoteDTOUpdateRequest noteDTOUpdateRequest) throws InvalidNoteDomainException, BadRequestException {
        NoteDomain noteDomain = NoteDomain.validate(
            noteDTOUpdateRequest.title,
            noteDTOUpdateRequest.content
        );

        Optional<UserModel> findUserModelByUserIdResponse = this.userRepository.findById(
            UUID.fromString(noteDTOUpdateRequest.userId)
        );

        if (findUserModelByUserIdResponse.isEmpty()) {
            throw new BadRequestException("Error: usuário não encontrado");
        }

        Optional<NoteModel> findNoteModelByNoteIdResponse = this.noteRepository.findById(
            UUID.fromString(noteDTOUpdateRequest.noteId)
        );

        if (findNoteModelByNoteIdResponse.isEmpty()) {
            throw new BadRequestException("Error: nota não encontrada");
        }

        NoteModel noteModel = new NoteModel(
            noteDomain.getTitle(),
            noteDomain.getContent(),
            findUserModelByUserIdResponse.get()
        );

        noteModel.noteId = UUID.fromString(noteDTOUpdateRequest.noteId);
        
        NoteModel saveNoteModelResponse = this.noteRepository.save(noteModel);

        return new NoteDTOUpdateResponse(
            saveNoteModelResponse.noteId.toString(),
            saveNoteModelResponse.title,
            saveNoteModelResponse.content
        );
    }
}
