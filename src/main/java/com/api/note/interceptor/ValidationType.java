package com.api.note.interceptor;

public enum ValidationType {
    ANNONYMOUS("Online");

    String value;

    ValidationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
