package com.api.note.controllers.note;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.note.dtos.note.NoteDTODeleteRequest;
import com.api.note.dtos.note.NoteDTODeleteResponse;
import com.api.note.dtos.note.NoteDTOFindByTitleRequest;
import com.api.note.dtos.note.NoteDTOFindByTitleResponse;
import com.api.note.dtos.note.NoteDTOFindByUserIdRequest;
import com.api.note.dtos.note.NoteDTOFindByUserIdResponse;
import com.api.note.dtos.note.NoteDTOSaveRequest;
import com.api.note.dtos.note.NoteDTOSaveResponse;
import com.api.note.dtos.note.NoteDTOUpdateRequest;
import com.api.note.dtos.note.NoteDTOUpdateResponse;
import com.api.note.services.note.NoteService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/note")
public class NoteController {
    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody NoteDTOSaveRequest noteDTOSaveRequest, Authentication authentication) {
        try {
            noteDTOSaveRequest.userId = authentication.getName();

            NoteDTOSaveResponse noteDTOSaveResponse = this.noteService.save(noteDTOSaveRequest);
        
            return ResponseEntity.status(HttpStatus.CREATED).body(noteDTOSaveResponse);
        } catch (Exception exception) {
            String message = exception.getMessage();

            if (message.equals("Error: usuário não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }

    @GetMapping
    public ResponseEntity<Object> findByUserId(Authentication authentication) {
        try {
            String userId = authentication.getName();

            NoteDTOFindByUserIdRequest noteDTOFindByUserIdRequest = new NoteDTOFindByUserIdRequest(userId);

            List<NoteDTOFindByUserIdResponse> noteDTOFindByUserIdResponse = this.noteService.findByUserId(noteDTOFindByUserIdRequest);

            return ResponseEntity.status(HttpStatus.OK).body(noteDTOFindByUserIdResponse);
        } catch (Exception exception) {
            String message = exception.getMessage();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @GetMapping("/title")
    public ResponseEntity<Object> findByTitle(@RequestBody NoteDTOFindByTitleRequest noteDTOFindByTitleRequest, Authentication authentication) {
        try {
            String userId = authentication.getName();

            noteDTOFindByTitleRequest.userId = userId;

            List<NoteDTOFindByTitleResponse> noteDTOFindByTitleResponse = this.noteService.findByTitle(noteDTOFindByTitleRequest);
            
            return ResponseEntity.status(HttpStatus.OK).body(noteDTOFindByTitleResponse);
        } catch (Exception exception) {
            String message = exception.getMessage();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody NoteDTOUpdateRequest noteDTOUpdateRequest, Authentication authentication) {
        try {
            String userId = authentication.getName();

            noteDTOUpdateRequest.userId = userId;

            NoteDTOUpdateResponse noteDTOUpdateResponse = this.noteService.update(noteDTOUpdateRequest);

            return ResponseEntity.status(HttpStatus.OK).body(noteDTOUpdateResponse);
        } catch (Exception exception) {
            if (exception.getMessage().equals("Error: nota n\u00E3o encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Object> delete(@PathVariable String noteId, Authentication authentication) {
        try {
            String userId = authentication.getName();
            
            NoteDTODeleteRequest noteDTODeleteRequest = new NoteDTODeleteRequest(noteId, userId);

            NoteDTODeleteResponse noteDTODeleteResponse = this.noteService.delete(noteDTODeleteRequest);

            return ResponseEntity.status(HttpStatus.OK).body(noteDTODeleteResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
