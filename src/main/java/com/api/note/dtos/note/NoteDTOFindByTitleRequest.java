package com.api.note.dtos.note;

public class NoteDTOFindByTitleRequest {
    public String title;
    public String userId;

    public NoteDTOFindByTitleRequest(String title, String userId) {
        this.title = title;
        this.userId = userId;
    }
}
