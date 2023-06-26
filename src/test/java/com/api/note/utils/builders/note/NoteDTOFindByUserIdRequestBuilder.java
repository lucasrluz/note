package com.api.note.utils.builders.note;

import java.util.UUID;

import com.api.note.dtos.note.NoteDTOFindByUserIdRequest;

public class NoteDTOFindByUserIdRequestBuilder {
    public static NoteDTOFindByUserIdRequest createWithValidData() {
        NoteDTOFindByUserIdRequest noteDTOFindByUserIdRequest = new NoteDTOFindByUserIdRequest(
            UUID.randomUUID().toString()
        );

        return noteDTOFindByUserIdRequest;
    }    
}
