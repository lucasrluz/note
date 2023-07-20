package com.api.note.utils.builders.auth;

import com.api.note.dtos.login.LoginDTORequest;

public class LoginDTORequestBuilder {
    public static LoginDTORequest createWithValidData() {
        return new LoginDTORequest("foobar@gmail.com", "123");
    }
}
