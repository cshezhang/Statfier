
class Parent {
  public static int counter;
}

class Child extends Parent {
  public Child() {
    Child.counter++;  // Noncompliant
  }
}
