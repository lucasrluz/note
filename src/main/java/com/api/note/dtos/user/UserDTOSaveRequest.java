package com.api.note.dtos.user;

public class UserDTOSaveRequest {
    public String name;
    public String email;
    public String password;

    public UserDTOSaveRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}