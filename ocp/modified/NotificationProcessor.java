package ocp.modified;
public class NotificationProcessor {
  public void processNotification(NotificationService notificationService, String message) {
    notificationService.sendNotification(message);
  }
}
