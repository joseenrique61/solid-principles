package ocp.modified.notificationServices;

import ocp.modified.NotificationService;

public class SmsService implements NotificationService {
  public void sendNotification(String message) {
    System.out.println("Sending SMS: " + message);
  }
}
