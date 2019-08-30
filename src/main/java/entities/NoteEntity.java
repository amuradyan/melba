package entities;

public final class NoteEntity {
    private String noteId;
    private String authorId;
    private String title;
    private String note;
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

    public String getNoteId() {
        return noteId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }
}
