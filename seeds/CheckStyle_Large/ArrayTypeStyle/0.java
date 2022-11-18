

public class MyClass {
  int[] nums; // OK
  String strings[]; // violation

  char[] toCharArray() { // OK
    return null;
  }

  byte getData()[] { // violation
    return null;
  }
}
        