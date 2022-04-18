

import java.util.Optional;

public class Main {

  static int field1 = 4; // OK
  public Optional<String> field2; // violation, usage of type 'Optional' is not allowed
  protected String field3; // OK
  Optional<String> field4; // violation, usage of type 'Optional' is not allowed
  private Optional<String> field5; // violation, usage of type 'Optional' is not allowed

  void foo() {
    Optional<String> i; // OK
  }
  public <T extends java.util.Optional> void method(T t) { // OK
    Optional<T> i; // OK
  }
}
        