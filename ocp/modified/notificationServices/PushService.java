package ocp.modified.notificationServices;

import ocp.modified.NotificationService;

public class PushService implements NotificationService {
  public void sendNotification(String message) {
    System.out.println("Sending Push: " + message);
  }
}
