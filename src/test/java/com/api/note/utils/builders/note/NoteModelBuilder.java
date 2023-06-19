package com.api.note.utils.builders.note;

import com.api.note.models.NoteModel;

public class NoteModelBuilder {
    public static NoteModel createWithValidData() {
        NoteModel noteModel = new NoteModel(
            "Foo Bar",
            "Foo bar",
            null
        );

        return noteModel;
    }
}
