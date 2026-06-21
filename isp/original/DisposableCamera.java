package isp.original;

class DisposableCamera implements Device {
  @Override
  public void turnOn() {
    System.out.println("Disposable camera is turning on.");
  }

  @Override
  public void turnOff() {
    System.out.println("Disposable camera is turning off.");
  }

  @Override
  public void charge() {
    // Disposable cameras cannot be charged, but they are forced to implement this
    // method.
    throw new UnsupportedOperationException("Disposable cameras cannot be charged.");
  }
}
