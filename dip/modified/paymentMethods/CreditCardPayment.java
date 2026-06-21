package dip.modified.paymentMethods;

import dip.modified.PaymentMethod;

public class CreditCardPayment implements PaymentMethod {
  @Override
  public void processPayment(double amount) {
    System.out.println("Processing credit card payment of $" + amount);
  }
}
