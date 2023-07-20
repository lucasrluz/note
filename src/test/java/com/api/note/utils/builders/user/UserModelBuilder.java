package com.api.note.utils.builders.user;

import com.api.note.models.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserModelBuilder {
    public static UserModel createWithValidData() {
        String hashPassword = BCrypt.withDefaults().hashToString(12, "123".toCharArray());
        
        UserModel userModel = new UserModel("Foo Bar", "foobar@gmail.com", hashPassword);

        return userModel;
    }
}
