package isp.modified;

import isp.modified.interfaces.Chargeable;
import isp.modified.interfaces.Device;

public class Main {
  public static void main(String[] args) {
    Device phone = new Phone();
    Device camera = new DisposableCamera();

    phone.turnOn();
    ((Chargeable) phone).charge();

    camera.turnOn();
  }
}
