package dip.modified;

import dip.modified.paymentMethods.CreditCardPayment;
import dip.modified.paymentMethods.CryptoPayment;
import dip.modified.paymentMethods.PayPalPayment;

public class Main {
  public static void main(String[] args) {
    PaymentMethod creditCardPayment = new CreditCardPayment();
    PaymentMethod payPalPayment = new PayPalPayment();
    PaymentMethod cryptoPayment = new CryptoPayment();

    PaymentProcessor creditCardProcessor = new PaymentProcessor(creditCardPayment);
    creditCardProcessor.makePayment(150.0);

    PaymentProcessor payPalProcessor = new PaymentProcessor(payPalPayment);
    payPalProcessor.makePayment(300.0);

    PaymentProcessor cryptoProcessor = new PaymentProcessor(cryptoPayment);
    cryptoProcessor.makePayment(500.0);
  }
}
