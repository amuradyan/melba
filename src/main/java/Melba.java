import com.google.gson.Gson;

import static spark.Spark.*;

public class Melba {
    private static Gson gson = new Gson();
    public static void main(String[] args) {
        get("/", (req, res) -> {
            res.type("application/json");
            return gson.toJson("It works!");
        });

        path("/users", () -> {
            // User creation
            post("", (req, res) -> "User created");

            // Notes CRUD
            path("/:user_id/notes", () -> {
                get("", (req, res) -> "Fetched all notes for user " + req.params(":user_id"));
                post("", (req, res) -> "Created a note for user " + req.params(":user_id"));
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
