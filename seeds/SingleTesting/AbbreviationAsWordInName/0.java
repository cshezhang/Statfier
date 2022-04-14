

public class MyClass extends SuperClass { // OK, camel case
  int CURRENT_COUNTER; // violation, at most 4 consecutive capital letters allowed
  static int GLOBAL_COUNTER; // OK, static is ignored
  final Set<String> stringsFOUND = new HashSet<>(); // OK, final is ignored

  @Override
  void printCOUNTER() { // OK, overridden method is ignored
    System.out.println(CURRENT_COUNTER); // OK, only definitions are checked
  }

  void incrementCOUNTER() { // violation, at most 4 consecutive capital letters allowed
    CURRENT_COUNTER++; // OK, only definitions are checked
  }

  static void incrementGLOBAL() { // violation, static method is not ignored
    GLOBAL_COUNTER++; // OK, only definitions are checked
  }

}
        