package com.api.note.dtos.user;

public class UserDTOSaveResponse {
    public String userId;
    public String name;
    public String email;

    public UserDTOSaveResponse(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}