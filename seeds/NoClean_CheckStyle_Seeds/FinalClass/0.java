

final class MyClass { // OK
  private MyClass() { }
}

class MyClass { // violation, class should be declared final
  private MyClass() { }
}

class MyClass { // OK, since it has a public constructor
  int field1;
  String field2;
  private MyClass(int value) {
    this.field1 = value;
    this.field2 = " ";
  }
  public MyClass(String value) {
    this.field2 = value;
    this.field1 = 0;
  }
}

class TestAnonymousInnerClasses { // OK, class has an anonymous inner class.
    public static final TestAnonymousInnerClasses ONE = new TestAnonymousInnerClasses() {

    };

    private TestAnonymousInnerClasses() {
    }
}
        