class MyClass {
  public static final int firstConstant = 10; // OK
  protected static final int secondConstant = 100; // OK
  static final int thirdConstant = 1000; // violation, name 'thirdConstant' must
  // match pattern '^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
  private static final int fourthConstant = 50; // violation, name 'fourthConstant' must match
  // pattern '^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
}

