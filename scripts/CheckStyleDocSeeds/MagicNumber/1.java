

@MyAnnotation(6) // no violation
class MyClass {
  private field = 7; // no violation

  void foo() {
    int i = i + 1; // no violation
    int j = j + 8; // violation
  }
}
        