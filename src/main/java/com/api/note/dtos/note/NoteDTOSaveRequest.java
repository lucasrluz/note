package com.api.note.dtos.note;

public class NoteDTOSaveRequest {
    public String title;
    public String content;
    public String userId;
    
    public NoteDTOSaveRequest(String title, String content, String userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
