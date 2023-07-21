package com.api.note.utils.builders.auth;

import java.util.UUID;

import com.api.note.dtos.auth.AuthenticateDTORequest;

public class AuthenticateDTORequestBuilder {
    public static AuthenticateDTORequest createWithValidData() {
        AuthenticateDTORequest authenticateDTOResponse = new AuthenticateDTORequest(
            "jwt",
            UUID.randomUUID().toString()
        );

        return authenticateDTOResponse;
    }
}
