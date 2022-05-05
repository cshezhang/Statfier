

public class Test {
  public void test() {
    System.out.println("Line 1");
    // another 47 lines of code
    System.out.println("Line 49");
  }

  public void test1() {
    System.out.println("Line 50");  // OK
    // another 47 lines of code
    System.out.println("Line 98"); // violation
  }
}
        