

public class Test1 {
  public void test() {
    System.out.println("Line 1");
    // another 48 lines of code
    System.out.println("Line 49");
  }

  public void test1() {
    System.out.println("Line 50");
    // another 47 lines of code
    System.out.println("Line 98"); // OK
  }
}

class Test2 {
  public void test() {
    System.out.println("Line 150"); // OK
  }

  public void test1() {
    System.out.println("Line 200"); // violation
  }
}
        