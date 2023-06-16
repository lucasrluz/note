package com.api.note.utils.builders.user;

import com.api.note.dtos.user.UserDTOSaveRequest;

public class UserDTOSaveRequestBuilder {
    public static UserDTOSaveRequest createWithValidData() {
        UserDTOSaveRequest userDTOSaveRequest = new UserDTOSaveRequest(
            "Foo Bar",
            "foobar@gmail.com",
            "123"
        );

        return userDTOSaveRequest;
    }

    public static UserDTOSaveRequest createWithEmptyName() {
        UserDTOSaveRequest userDTOSaveRequest = new UserDTOSaveRequest(
            "",
            "foobar@gmail.com",
            "123"
        );

        return userDTOSaveRequest;
    }

    public static UserDTOSaveRequest createWithEmptyEmail() {
        UserDTOSaveRequest userDTOSaveRequest = new UserDTOSaveRequest(
            "Foo Bar",
            "",
            "123"
        );

        return userDTOSaveRequest;
    }

    public static UserDTOSaveRequest createWithInvalidEmailFormat() {
        UserDTOSaveRequest userDTOSaveRequest = new UserDTOSaveRequest(
            "Foo Bar",
            "@gmail.com",
            "123"
        );

        return userDTOSaveRequest;
    }

    public static UserDTOSaveRequest createWithEmptyPassword() {
        UserDTOSaveRequest userDTOSaveRequest = new UserDTOSaveRequest(
            "Foo Bar",
            "foobar@gmail.com",
            ""
        );

        return userDTOSaveRequest;
    }
}
