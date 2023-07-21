package com.api.note.dtos.auth;

public class LoginDTOResponse {
    public String jwt;

    public LoginDTOResponse(String jwt) {
        this.jwt = jwt;
    }
}
