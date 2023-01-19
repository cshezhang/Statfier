class MyClass extends SomeOtherClass {
  MyClass() {
    super(); // violation
  }

  MyClass(int arg) {
    super(arg); // OK, call with argument have to be explicit
  }

  MyClass(long arg) {
    // OK, call is implicit
  }
}

