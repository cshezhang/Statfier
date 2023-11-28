class MyClass {
  public int num1; // violation, name 'num1'
  // must match pattern '^m[A-Z][a-zA-Z0-9]*$'
  protected int num2; // OK
  int num3; // OK
  private int num4; // violation, name 'num4'
  // must match pattern '^m[A-Z][a-zA-Z0-9]*$'
}

