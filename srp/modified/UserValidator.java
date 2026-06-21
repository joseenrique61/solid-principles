package srp.modified;

import srp.modified.validators.EmailValidator;
import srp.modified.validators.PasswordValidator;

class UserValidator {
  public static boolean validateUser(String email, String password) {
    return EmailValidator.isValidEmail(email) && PasswordValidator.isValidPassword(password);
  }
}
