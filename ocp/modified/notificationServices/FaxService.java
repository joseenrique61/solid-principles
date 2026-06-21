package ocp.modified.notificationServices;

import ocp.modified.NotificationService;

public class FaxService implements NotificationService {
  public void sendNotification(String message) {
    System.out.println("Sending Fax: " + message);
  }
}
