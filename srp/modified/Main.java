package srp.modified;

import srp.modified.models.User;

public class Main {
  public static void main(String[] args) {
    DatabaseManager databaseManager = new DatabaseManager();
    EmailSender emailSender = new EmailSender();

    UserController userController = new UserController(databaseManager, emailSender);

    userController
        .addUsers(new User[] {
            new User("example@domain.com", "password123"),
            new User("invalid-email", "1234")
        });
  }
}
