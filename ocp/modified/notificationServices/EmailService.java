package ocp.modified.notificationServices;

import ocp.modified.NotificationService;

public class EmailService implements NotificationService {
  public void sendNotification(String message) {
    System.out.println("Sending Email: " + message);
  }
}
