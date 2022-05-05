

public class MyClass {
  int[] nums; // violation
  String strings[]; // OK

  char[] toCharArray() { // OK
    return null;
  }

  byte getData()[] { // violation
    return null;
  }
}
        