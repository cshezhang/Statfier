

public class MyClass {
  String a = "StringContents";
  String a1 = "unchecked";
  @SuppressWarnings("unchecked") // violation, "unchecked" occurs twice
  public void myTest() {
    String a2 = "StringContents"; // violation, "StringContents" occurs twice
    String a3 = "DoubleString" + "DoubleString"; // violation, "DoubleString" occurs twice
    String a4 = "SingleString"; // OK
    String a5 = ", " + ", " + ", "; // violation, ", " occurs three times
  }
}
        