package lsp.modified;

public class Main {
  public static void main(String[] args) {
    Animal dog = new Dog();
    Animal fish = new Fish();

    dog.makeSound();
    ((Dog) dog).walk();

    fish.makeSound();
  }
}
