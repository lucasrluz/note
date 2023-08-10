package com.api.note.dtos.note;

public class NoteDTOFindByUserIdResponse {
    public String noteId;
    public String title;
    public String content;

    public NoteDTOFindByUserIdResponse(String noteId, String title, String content) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
    }
}
