package com.api.note.dtos.note;

public class NoteDTODeleteRequest {
    public String noteId;
    public String userId;

    public NoteDTODeleteRequest(String noteId, String userId) {
        this.noteId = noteId;
        this.userId = userId;
    }

    public NoteDTODeleteRequest() {}
}
