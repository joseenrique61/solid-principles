package ocp.original;

public class Main {
  public static void main(String[] args) {
    NotificationService service = new NotificationService();
    service.sendNotification("Email", "Hello via Email!");
    service.sendNotification("SMS", "Hello via SMS!");
    service.sendNotification("Push", "Hello via Push Notification!");
    service.sendNotification("Fax", "Hello via Fax!");
  }
}
