package srp.modified;

import srp.modified.models.User;

public class UserController {
  private DatabaseManager databaseManager;
  private EmailSender emailSender;

  public UserController(DatabaseManager databaseManager, EmailSender emailSender) {
    this.databaseManager = databaseManager;
    this.emailSender = emailSender;
  }

  public void addUsers(User[] users) {
    for (User user : users) {
      addUserAndSendEmail(user.getEmail(), user.getPassword());
    }
  }

  private void addUserAndSendEmail(String email, String password) {
    if (!UserValidator.validateUser(email, password)) {
      System.out.println("Invalid email or password. User not added.");
      return;
    }

    databaseManager.saveToDatabase(email, password);
    emailSender.sendWelcomeEmail(email);
  }
}
