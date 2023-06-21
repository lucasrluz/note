package com.api.note.services.note;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.note.domains.note.NoteDomain;
import com.api.note.domains.note.exceptions.InvalidNoteDomainException;
import com.api.note.dtos.note.NoteDTOSaveRequest;
import com.api.note.dtos.note.NoteDTOSaveResponse;
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
}
