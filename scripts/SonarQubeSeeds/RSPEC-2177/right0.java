
// Parent.java
import computer.Pear;
public class Parent {

  public void doSomething(Pear p) {
    //,,,
  }

  public static void doSomethingElse() {
    //...
  }
}

// Child.java
import computer.Pear;  // import corrected
public class Child extends Parent {

  public void doSomething(Pear p) {  // true override (see import)
    //,,,
  }

  public static void doSomethingElse() {
    //...
  }
}
