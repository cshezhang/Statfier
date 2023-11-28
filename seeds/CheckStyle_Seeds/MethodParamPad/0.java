public class Test {
  public Test() { // OK
    super(); // OK
  }

  public Test(int aParam) { // Violation - '(' is preceded with whitespace
    super(); // Violation - '(' is preceded with whitespace
  }

  public void method() {} // OK

  public void methodWithVeryLongName() {} // Violation - '(' is preceded with whitespace
}

