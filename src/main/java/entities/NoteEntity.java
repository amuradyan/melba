package entities;

import models.Note;

public final class NoteEntity extends Note {
  private String noteId;
  private String authorId;
  private Long createdAt;
  private Long updatedAt;

  public NoteEntity(String noteId, String authorId, String title, String note, Long createdAt, Long updatedAt) {
    this.noteId = noteId;
    this.authorId = authorId;
    this.title = title;
    this.note = note;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
