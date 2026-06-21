package dip.modified;

public class PaymentProcessor {
  private PaymentMethod paymentMethod;

  public PaymentProcessor(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public void makePayment(double amount) {
    paymentMethod.processPayment(amount);
  }
}
