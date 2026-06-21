package isp.original;

public class Main {
  public static void main(String[] args) {
    Device phone = new Phone();
    Device camera = new DisposableCamera();

    phone.turnOn();
    phone.charge();

    camera.turnOn();
    camera.charge(); // Excepción: UnsupportedOperationException
  }
}
