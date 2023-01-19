public class Test {

  public void test() {

    if (foo) {
      bar();
    } else { // violation, right curly must be alone on line
      bar();
    }

    if (foo) {
      bar();
    } // OK
    else {
      bar();
    }

    try {
      bar();
    } catch (Exception e) { // OK because config did not set token LITERAL_TRY
      bar();
    }
  } // OK

  public void violate() {
    bar();
  } // OK , because singleline
}

