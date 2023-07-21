package com.api.note.utils.builders.auth;

import com.api.note.dtos.auth.LoginDTORequest;

public class LoginDTORequestBuilder {
    public static LoginDTORequest createWithValidData() {
        return new LoginDTORequest("foobar@gmail.com", "123");
    }
}
