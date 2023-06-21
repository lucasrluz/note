package com.api.note.controllers.note;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.note.dtos.note.NoteDTOSaveRequest;
import com.api.note.dtos.note.NoteDTOSaveResponse;
import com.api.note.services.note.NoteService;

@RestController
@RequestMapping("/note")
public class NoteController {
    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Object> save(@RequestBody NoteDTOSaveRequest noteDTOSaveRequest, @PathVariable String userId) {
        try {
            noteDTOSaveRequest.userId = userId;

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
}