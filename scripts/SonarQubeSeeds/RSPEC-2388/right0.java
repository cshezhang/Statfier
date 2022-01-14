
public class Parent {
  public void foo() { ... }
}

public class Outer {

  public void foo() { ... }

  public class Inner extends Parent {

    public void doTheThing() {
      super.foo();
      // ...
    }
  }
}
