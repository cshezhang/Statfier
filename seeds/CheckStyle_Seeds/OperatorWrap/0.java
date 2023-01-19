class Test {
  public static void main(String[] args) {
    String s = "Hello" + "World"; // violation, '+' should be on new line

    if (10 == 20) { // violation, '==' should be on new line.
      // body
    }
    if (10 == 20) { // ok
      // body
    }

    int c = 10 / 5; // violation, '/' should be on new line.

    int d = c + 10; // ok
  }
}

