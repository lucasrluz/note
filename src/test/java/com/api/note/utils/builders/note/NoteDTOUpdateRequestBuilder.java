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

    public static NoteDTOUpdateRequest createWithEmptyTitle() {
        NoteDTOUpdateRequest noteDTOUpdateRequest = new NoteDTOUpdateRequest(
            null,
            null,
            "",
            "Bar foo"
        );

        return noteDTOUpdateRequest;
    }

    public static NoteDTOUpdateRequest createWithEmptyContent() {
        NoteDTOUpdateRequest noteDTOUpdateRequest = new NoteDTOUpdateRequest(
            null,
            null,
            "Bar Foo",
            ""
        );

        return noteDTOUpdateRequest;
    }
}
