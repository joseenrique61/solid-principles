package lsp.modified;

public class Dog extends Animal implements IWalkingAnimal {
  @Override
  public void walk() {
    System.out.println("Dog walks.");
  }

  @Override
  public void makeSound() {
    System.out.println("Dog barks.");
  }
}
