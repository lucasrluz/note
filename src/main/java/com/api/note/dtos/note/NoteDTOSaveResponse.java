package com.api.note.dtos.note;

public class NoteDTOSaveResponse {
   public String noteId;
   public String title;
   
   public NoteDTOSaveResponse(String noteId, String title) {
      this.noteId = noteId;
      this.title = title;
   }
}
