

class Test
{ // Violation - '{' should be on the previous line
  private interface TestInterface
  { // Violation - '{' should be on the previous line
  }

  private
  class
  MyClass { // OK
  }

  enum Colors {RED, // Violation - '{' should have line break after
  BLUE,
  GREEN;
  }
}
        