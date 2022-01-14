
public class Parent {

  synchronized void foo() {
    //...
  }
}

public class Child extends Parent {

  @Override
  synchronized void foo () {
    // ...
    super.foo();
  }
}
