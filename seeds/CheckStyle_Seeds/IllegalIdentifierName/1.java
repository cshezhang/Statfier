public class TestClass {
  public static void main(String... args) {

    int open = 4; // violation, "open" should not be used as an identifier
    Object transitive = "transitive"; // violation, "transitive" should not
    // be used as an identifier

    int openInt = 4; // ok, "open" is part of another word
    Object transitiveObject = "transitiveObject"; // ok, "transitive" is part of another word
  }
}

