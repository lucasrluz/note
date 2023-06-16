package com.api.note.utils.builders.user;

import com.api.note.models.UserModel;

public class UserModelBuilder {
    public static UserModel createWithValidData() {
        UserModel userModel = new UserModel("Foo Bar", "foobar@gmail.com", "123");

        return userModel;
    }
}
