package com.api.note.dtos.login;

public class LoginDTORequest {
    public String email;
    public String password;
    
    public LoginDTORequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
