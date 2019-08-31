package services;

import entities.NoteEntity;
import specs.NoteSpec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NoteManagement {
  private static final Logger logger = Logger.getLogger(NoteManagement.class.getName());

  private NoteManagement() {
  }

  public static NoteEntity createNote(String authorId, NoteSpec noteSpec) {
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

      if (res > 0) {
        note = new NoteEntity(noteId, authorId, noteSpec.getTitle(), noteSpec.getNote(), createdAt, updatedAt);
      }
    } catch (SQLException e) {
      logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

    return note;
  }

  public static NoteEntity getNoteByAuthorAndNoteIds(String authorId, String noteId) {
    NoteEntity note = null;

    try {
      Connection conn = DataSource.getConnection();
      PreparedStatement ps = conn.prepareStatement("select * from melba.notes where uid=? and nid=?;");
      ps.setString(1, authorId);
      ps.setString(2, noteId);
      ResultSet rs = ps.executeQuery();

      if (rs.next())
        note = new NoteEntity(rs.getString("nid"),
                rs.getString("uid"),
                rs.getString("title"),
                rs.getString("note"),
                rs.getLong("createdAt"),
                rs.getLong("updatedAt"));
    } catch (SQLException e) {
      logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

    return note;
  }

  public static boolean deleteNoteByAuthorAndNoteIds(String authorId, String noteId) {
    boolean res = false;

    try {
      Connection conn = DataSource.getConnection();
      PreparedStatement ps = conn.prepareStatement("delete from melba.notes where uid=? and nid=?;");
      ps.setString(1, authorId);
      ps.setString(2, noteId);
      int affectedRows = ps.executeUpdate();

      res = affectedRows > 0;
    } catch (SQLException e) {
      logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

    return res;
  }

  public static NoteEntity updateNote(String noteId, NoteSpec noteSpec) {
    return null;
  }
}
