package com.api.note.dtos.note;

public class NoteDTOFindByTitleResponse {
    public String title;
    public String content;

    public NoteDTOFindByTitleResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
