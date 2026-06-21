package srp.modified.validators;

import java.util.regex.Pattern;

public class EmailValidator {
  public static boolean isValidEmail(String email) {
    return Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email);
  }
}
