
public class Parent {

  synchronized void foo() {
    //...
  }
}

public class Child extends Parent {

 @Override
  public void foo () {  // Noncompliant
    // ...
    super.foo();
  }
}
