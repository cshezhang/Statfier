
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
import fruit.Pear;
public class Child extends Parent {

  public void doSomething(Pear p) {  // Noncompliant; this is not an override
    // ...
  }


  public void doSomethingElse() {  // Noncompliant; parent method is static
    //...
  }
}
