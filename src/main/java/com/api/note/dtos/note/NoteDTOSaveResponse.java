package com.api.note.dtos.note;

public class NoteDTOSaveResponse {
   public String noteId;
   public String title;
   public String content;
   
   public NoteDTOSaveResponse(String noteId, String title, String content) {
      this.noteId = noteId;
      this.title = title;
      this.content = content;
   }
}
