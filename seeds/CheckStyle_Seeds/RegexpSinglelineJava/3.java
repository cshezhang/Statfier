public class Foo {
  public void bar() {
    Logger.log("first"); // OK, first occurrence is allowed
    Logger.log("second"); // OK, second occurrence is allowed
    Logger.log("third"); // violation
    System.out.println("fourth");
    Logger.log("fifth"); // violation
  }
}

