package com.api.note.utils.builders.note;

import com.api.note.dtos.note.NoteDTOUpdateRequest;

public class NoteDTOUpdateRequestBuilder {
    public static NoteDTOUpdateRequest createWithValidData() {
        NoteDTOUpdateRequest noteDTOUpdateRequest = new NoteDTOUpdateRequest(
            null,
            null,
            "Foo Bar",
            "Foo bar"
        );

        return noteDTOUpdateRequest;
    }
}
