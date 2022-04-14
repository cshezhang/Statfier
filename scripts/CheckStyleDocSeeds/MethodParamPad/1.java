

public class Test {
  public Test() { // OK
    super(); // OK
  }

  public Test (int aParam) { // OK
    super (); // OK
  }

  public void method() {} // Violation - '(' is NOT preceded with whitespace

  public void methodWithVeryLongName
    () {} // OK, because allowLineBreaks is true

}
        