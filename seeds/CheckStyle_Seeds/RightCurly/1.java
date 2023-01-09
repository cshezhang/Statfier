

public class Test {

  public void test() {

    if (foo) {
      bar();
    } else { bar(); }   // violation, right curly must be alone on line

    if (foo) {
      bar();
    } else {
      bar();
    }                   // OK

    try {
      bar();
    } catch (Exception e) { // OK because config is set to token METHOD_DEF and LITERAL_ELSE
      bar();
    }

  }                         // OK

  public void violate() { bar; } // violation, singleline is not allowed here

  public void ok() {
    bar();
  }                              // OK
}
        