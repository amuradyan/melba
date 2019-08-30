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
            // User creation
            post("", (req, res) -> {
                UserSpec userSpec = UserSpec.fromJson(req.body());
                res.type("application/json");
                String body = "";
                if(userSpec != null && userSpec.isValid()) {
                    try {
                        UserEntity userEntity = UserManagement.createUser(userSpec);

                        if(userEntity != null) {
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
            path("/:user_id/notes", () -> {
                get("", (req, res) -> "Fetched all notes for user " + req.params(":user_id"));
                post("", (req, res) -> {
                    res.type("application/json");
                    String body = "";
                    String userId = req.params(":user_id");

                    if(UserManagement.userExists(userId)){
                        NoteSpec noteSpec = NoteSpec.fromJson(req.body());
                        if(noteSpec != null && noteSpec.isValid()) {
                            NoteEntity noteEntity = NoteManagement.createNote(userId, noteSpec);
                            body = gson.toJson(noteEntity);
                        } else {
                            res.status(400);
                        }
                    } else {
                        res.status(404);
                    }

                    return body;
                });
                delete("", (req, res) -> "Deleted all notes for user " + req.params(":user_id"));

                path("/:note_id", () -> {
                    get("", (req, res) -> "Fetched note " + req.params(":note_id") + " for user " + req.params(":user_id"));
                    delete("", (req, res) -> "Deleted note " + req.params(":note_id") + " for user " + req.params(":user_id"));
                    patch("", (req, res) -> "Updated note " + req.params(":note_id") + " for user " + req.params(":user_id"));
                });
            });
        });
    }
}
