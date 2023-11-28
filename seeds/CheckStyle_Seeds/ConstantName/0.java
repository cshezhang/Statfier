class MyClass {
  public static final int FIRST_CONSTANT1 = 10; // OK
  protected static final int SECOND_CONSTANT2 = 100; // OK
  static final int third_Constant3 = 1000; // violation, name 'third_Constant3' must
  // match pattern '^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
  private static final int fourth_Const4 = 50; // violation, name 'fourth_Const4' must match
  // pattern '^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
}

