package ocp.modified.notificationServices;

import ocp.modified.NotificationService;

public class WhatsappService implements NotificationService {
  public void sendNotification(String message) {
    System.out.println("Sending Whatsapp: " + message);
  }
}
