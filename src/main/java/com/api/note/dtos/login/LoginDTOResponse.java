package com.api.note.dtos.login;

public class LoginDTOResponse {
    public String jwt;

    public LoginDTOResponse(String jwt) {
        this.jwt = jwt;
    }
}
