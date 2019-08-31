package services;

import entities.NoteEntity;
import specs.NoteSpec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

  public static List<NoteEntity> getNotesByAuthor(String authorId) {
    List<NoteEntity> notes = new ArrayList<>();

    try {
      Connection conn = DataSource.getConnection();
      PreparedStatement ps = conn.prepareStatement("select * from melba.notes where uid=?;");
      ps.setString(1, authorId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        NoteEntity note = new NoteEntity(rs.getString("nid"),
                rs.getString("uid"),
                rs.getString("title"),
                rs.getString("note"),
                rs.getLong("createdAt"),
                rs.getLong("updatedAt"));

        notes.add(note);
      }
    } catch (SQLException e) {
      logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

    return notes;
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

  public static boolean deleteNote(String authorId, String noteId) {
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

  public static boolean deleteNotesOf(String authorId) {
    boolean res = false;

    try {
      Connection conn = DataSource.getConnection();
      PreparedStatement ps = conn.prepareStatement("delete from melba.notes where uid=?;");
      ps.setString(1, authorId);
      int affectedRows = ps.executeUpdate();

      res = affectedRows > 0;
    } catch (SQLException e) {
      logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

    return res;
  }

  public static NoteEntity updateNote(String authorId, String noteId, NoteSpec noteSpec) {
    NoteEntity note = null;
    Long updatedAt = System.currentTimeMillis();

    try {
      Connection conn = DataSource.getConnection();
      PreparedStatement ps =
              conn.prepareStatement("update melba.notes set title=?, note=?, updatedAt=? where uid=? and nid=?");
      ps.setString(1, noteSpec.getTitle());
      ps.setString(2, noteSpec.getNote());
      ps.setLong(3, updatedAt);
      ps.setString(4, authorId);
      ps.setString(5, noteId);

      int rowsUpdated = ps.executeUpdate();

      if (rowsUpdated > 0)
        note = getNoteByAuthorAndNoteIds(authorId, noteId);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return note;
  }
}
