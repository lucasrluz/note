package com.api.note.utils.builders.note;

import com.api.note.dtos.note.NoteDTOSaveRequest;

public class NoteDTOSaveRequestBuilder {
    public static NoteDTOSaveRequest createWithValidData() {
        NoteDTOSaveRequest noteDTOSaveRequest = new NoteDTOSaveRequest(
            "Foo Bar",
            "Foo bar",
            null
        );

        return noteDTOSaveRequest;
    }

    public static NoteDTOSaveRequest createWithEmptyTitle() {
        NoteDTOSaveRequest noteDTOSaveRequest = new NoteDTOSaveRequest(
            "",
            "Foo bar",
            null
        );

        return noteDTOSaveRequest;
    }

    public static NoteDTOSaveRequest createWithEmptyContent() {
        NoteDTOSaveRequest noteDTOSaveRequest = new NoteDTOSaveRequest(
            "Foo Bar",
            "",
            null
        );

        return noteDTOSaveRequest;
    }
}
