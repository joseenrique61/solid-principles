package ocp.modified;

import ocp.modified.notificationServices.EmailService;
import ocp.modified.notificationServices.FaxService;
import ocp.modified.notificationServices.PushService;
import ocp.modified.notificationServices.SmsService;
import ocp.modified.notificationServices.WhatsappService;

public class Main {
  public static void main(String[] args) {
    NotificationProcessor notificationProcessor = new NotificationProcessor();
    notificationProcessor.processNotification(new EmailService(), "Hello via Email!");
    notificationProcessor.processNotification(new SmsService(), "Hello via SMS!");
    notificationProcessor.processNotification(new PushService(), "Hello via Push Notification!");
    notificationProcessor.processNotification(new FaxService(), "Hello via Fax!");
    notificationProcessor.processNotification(new WhatsappService(), "Hello via Whatsapp!");
  }
}