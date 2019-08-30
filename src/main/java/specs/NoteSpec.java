package specs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class NoteSpec {
    private static final Logger logger = Logger.getLogger(NoteSpec.class.getName());

    private String title;
    private String note;

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

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
