package services;

import entities.NoteEntity;
import specs.NoteSpec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NoteManagement {
    private static final Logger logger = Logger.getLogger(NoteManagement.class.getName());

    private NoteManagement() {}

    public static NoteEntity createNote(String authorId,NoteSpec noteSpec) {
        NoteEntity note = null;
        try {
            Connection conn = DataSource.getConnection();
            String noteId = UUID.randomUUID().toString();
            Long createdAt = System.currentTimeMillis();
            Long updatedAt = createdAt;

            PreparedStatement ps = conn.prepareStatement("insert into melba.notes value (?, ?, ?, ?, ?, ?)");
            ps.setString(1, noteId);
            ps.setString(2, authorId);
            ps.setString(3, noteSpec.getTitle());
            ps.setString(4, noteSpec.getNote());
            ps.setLong(5, createdAt);
            ps.setLong(6, updatedAt);

            int res = ps.executeUpdate();

            if(res > 0) {
                note = new NoteEntity(noteId, authorId, noteSpec.getTitle(), noteSpec.getNote(), createdAt, updatedAt);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return note;
    }

    public static NoteEntity getNoteById(String noteId) {
        return null;
    }

    public static boolean deleteNote(String noteId) {
        return false;
    }

    public static NoteEntity updateNote(NoteSpec noteSpec) {
        return null;
    }
}
