package com.api.note.dtos.note;

public class NoteDTOUpdateRequest {
    public String userId;
    public String noteId;
    public String title;
    public String content;
    
    public NoteDTOUpdateRequest(String userId, String noteId, String title, String content) {
        this.userId = userId;
        this.noteId = noteId;
        this.title = title;
        this.content = content;
    }    
}
