import com.google.gson.Gson;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import entities.NoteEntity;
import entities.UserEntity;
import services.NoteManagement;
import services.UserManagement;
import specs.NoteSpec;
import specs.UserSpec;
import views.UserView;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Base64;
import java.util.List;

import static spark.Spark.*;

public class Melba {
  private static Config config = ConfigFactory.load().resolve();

  private static Gson gson = new Gson();

  public static void main(String[] args) {
    port(config.getInt("app.port"));

    get("/", (req, res) -> {
      res.type("application/json");
      return gson.toJson("It works!");
    });

    path("/users", () -> {
      post("", (req, res) -> {
        UserSpec userSpec = UserSpec.fromJson(req.body());
        res.type("application/json");
        String body = "";
        if (userSpec != null && userSpec.isValid()) {
          try {
            UserEntity userEntity = UserManagement.createUser(userSpec);

            if (userEntity != null) {
              res.status(201);
              body = gson.toJson(UserView.fromEntity(userEntity));
            } else {
              res.status(500);
              body = "Unable to create a user";
            }
          } catch (SQLIntegrityConstraintViolationException e) {
            res.status(422);
            body = "Email must be unique. You are already registered.";
          }
        } else {
          res.status(400);
        }

        return body;
      });

      // Notes CRUD
      path("/:user_id", () -> {
        // Auth
        before("/*", (request, response) -> {
          String authHeader = request.headers("Authorization");
          String userId = request.params(":user_id");
          if (authHeader != null) {
            String[] authHeaderDecomposed = authHeader.split(" ");

            if (authHeaderDecomposed.length == 2 && authHeaderDecomposed[0].equals("Basic")) {
              String credentialsDecoded = null;
              try {
                credentialsDecoded = new String(Base64.getDecoder().decode(authHeaderDecomposed[1]));
              } catch (Exception e) {
                halt(401, "Invalid credentials base64");
              }

              String[] credentials = credentialsDecoded.split(":");

              if (credentials.length == 2) {
                String email = credentials[0];
                String password = credentials[1];

                UserEntity userEntity = UserManagement.getUser(email, password);

                if (userEntity == null) {
                  halt(401, "Invalid email/password");
                }

                if (!userEntity.getId().equals(userId))
                  halt(401, "You can access only your resources");
              } else
                halt(401, "Invalid credentials structure");
            } else
              halt(401, "Unsupported authorization schema");
          } else
            halt(401, "Missing authorization header");
        });

        path("/notes", () -> {
          get("", (req, res) -> {
            res.type("application/json");
            String body = "";
            List<NoteEntity> notes = NoteManagement.getNotesByAuthor(req.params(":user_id"));

            if (notes.size() == 0)
              res.status(404);
            else
              body = gson.toJson(notes);

            return body;
          });

          post("", (req, res) -> {
            res.type("application/json");
            String body = "";
            String userId = req.params(":user_id");

            NoteSpec noteSpec = NoteSpec.fromJson(req.body());
            if (noteSpec != null && noteSpec.isValid()) {
              NoteEntity noteEntity = NoteManagement.createNote(userId, noteSpec);

              if (noteEntity != null)
                body = gson.toJson(noteEntity);
              else
                res.status(500);
            } else {
              res.status(400);
            }

            return body;
          });

          delete("", (req, res) -> {
            res.type("application/json");

            boolean resourceRemoved =
                    NoteManagement.deleteNotesOf(req.params(":user_id"));

            if (resourceRemoved)
              res.status(200);
            else
              res.status(404);

            return "";
          });

          path("/:note_id", () -> {
            get("", (req, res) -> {
              res.type("application/json");
              String body = "";
              NoteEntity note = NoteManagement.getNoteByAuthorAndNoteIds(req.params(":user_id"),
                      req.params(":note_id"));

              if (note == null)
                res.status(404);
              else
                body = gson.toJson(note, NoteEntity.class);

              return body;
            });

            delete("", (req, res) -> {
              res.type("application/json");

              boolean resourceRemoved =
                      NoteManagement.deleteNote(req.params(":user_id"), req.params(":note_id"));

              if (resourceRemoved)
                res.status(200);
              else
                res.status(404);

              return "";
            });

            patch("", (req, res) -> {
              res.type("application/json");
              String body = "";

              NoteSpec noteSpec = NoteSpec.fromJson(req.body());
              if (noteSpec != null && noteSpec.isValid()) {
                NoteEntity updatedNote =
                        NoteManagement.updateNote(req.params(":user_id"), req.params(":note_id"), noteSpec);
                if (updatedNote != null)
                  body = gson.toJson(updatedNote);
                else
                  res.status(500);
              } else {
                res.status(400);
              }

              return body;
            });
          });
        });
      });
    });
  }
} 
