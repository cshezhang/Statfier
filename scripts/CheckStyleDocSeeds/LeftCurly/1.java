

class Test
{ // OK
  private interface TestInterface
  { // OK
  }

  private
  class
  MyClass { // Violation - '{' should be on a new line
  }

  enum Colors {RED, // OK
    BLUE,
    GREEN;
  }
}
        