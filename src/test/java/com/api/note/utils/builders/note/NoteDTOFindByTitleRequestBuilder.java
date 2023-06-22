package com.api.note.utils.builders.note;

import com.api.note.dtos.note.NoteDTOFindByTitleRequest;

public class NoteDTOFindByTitleRequestBuilder {
    public static NoteDTOFindByTitleRequest createWithValidData() {
        NoteDTOFindByTitleRequest noteDTOFindByTitleRequest = new NoteDTOFindByTitleRequest(
            "Foo Bar",
            null
        );

        return noteDTOFindByTitleRequest;
    }
}
