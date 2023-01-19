class MyClass {
  public void myMethod() {} // OK

  public void MyMethod() {} // violation, name "MyMethod"
  // should match the pattern "^[a-z](_?[a-zA-Z0-9]+)*$"
}

