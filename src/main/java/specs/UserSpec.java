package specs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class UserSpec {
    private static final Logger logger = Logger.getLogger(UserSpec.class.getName());

    private String email;
    private String password;
    private Long createdAt;
    private Long updatedAt;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public static UserSpec fromJson(String userSpecJson) {
        UserSpec userSpec = null;
        try {
            userSpec = new Gson().fromJson(userSpecJson, UserSpec.class);
        } catch (JsonSyntaxException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return userSpec;
    }

    public boolean isValid() {
        return EmailValidator.getInstance().isValid(email) &&
                !password.trim().isEmpty() && password.trim().length() <= 8 &&
                createdAt != null && updatedAt != null;
    }
}
