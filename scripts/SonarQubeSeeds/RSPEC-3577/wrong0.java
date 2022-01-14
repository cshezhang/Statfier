
class Foo {  // Noncompliant
  @Test
  void check() {  }
}

class Bar {  // Noncompliant
  @Nested
  class PositiveCase {
    @Test
    void check() {  }
  }
}
