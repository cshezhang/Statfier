

@MyAnnotation(6) // violation
class MyClass {
  private field = 7; // violation

  void foo() {
    int i = i + 1; // no violation
    int j = j + 8; // violation
  }

  public int hashCode() {
    return 10;    // violation
  }
}
@interface anno {
  int value() default 10; // no violation
}
        