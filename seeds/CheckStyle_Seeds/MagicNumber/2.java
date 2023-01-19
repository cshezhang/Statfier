public record MyRecord() {
  private static int myInt = 7; // ok, field declaration

  void foo() {
    int i = myInt + 1; // no violation, 1 is defined as non-magic
    int j = myInt + 8; // violation
  }
}

