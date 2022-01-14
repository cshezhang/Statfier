
class Parent {
  public static int counter;
}

class Child extends Parent {
  public Child() {
    Parent.counter++;
  }
}
