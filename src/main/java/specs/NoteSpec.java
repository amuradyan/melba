package specs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import models.Note;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class NoteSpec extends Note {
  private static final Logger logger = Logger.getLogger(NoteSpec.class.getName());

  public static NoteSpec fromJson(String noteSpecJson) {
    NoteSpec noteSpec = null;

    try {
      noteSpec = new Gson().fromJson(noteSpecJson, NoteSpec.class);
    } catch (JsonSyntaxException e) {
      logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

    return noteSpec;
  }

  public boolean isValid() {
    return title != null && !title.trim().isEmpty() && title.length() <= 50 &&
            note != null && note.length() <= 1000;
  }
}
