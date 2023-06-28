package com.api.note.dtos.note;

public class NoteDTOUpdateResponse {
    public String noteId;
    public String title;
    public String content;
    
    public NoteDTOUpdateResponse(String noteId, String title, String content) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
    }
}
