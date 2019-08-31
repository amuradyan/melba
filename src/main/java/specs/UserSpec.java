package specs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import models.User;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class UserSpec extends User {
  private static final Logger logger = Logger.getLogger(UserSpec.class.getName());

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
    return email != null && EmailValidator.getInstance().isValid(email) &&
            password != null && !password.trim().isEmpty() && password.trim().length() >= 8;
  }
}
