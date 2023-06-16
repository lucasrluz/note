package com.api.note.services.util;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
