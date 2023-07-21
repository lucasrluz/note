package com.api.note.dtos.auth;

public class AuthenticateDTORequest {
    public String jwt;
    public String userId;
    
    public AuthenticateDTORequest(String jwt, String userId) {
        this.jwt = jwt;
        this.userId = userId;
    }
}
