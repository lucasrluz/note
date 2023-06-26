package com.api.note.dtos.note;

public class NoteDTOFindByUserIdResponse {
    public String title;
    public String content;

    public NoteDTOFindByUserIdResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
