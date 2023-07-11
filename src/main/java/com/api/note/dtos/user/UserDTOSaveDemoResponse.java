package com.api.note.dtos.user;

public class UserDTOSaveDemoResponse {
    public String userId;
    public String name;
    public String email;
    public String password;

    public UserDTOSaveDemoResponse(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
