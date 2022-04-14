

class MyClass {
  public final static int firstConstant = 10; // OK
  protected final static int secondConstant = 100; // OK
  final static int thirdConstant = 1000; // violation, name 'thirdConstant' must
                                         // match pattern '^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
  private final static int fourthConstant = 50; // violation, name 'fourthConstant' must match
                                                // pattern '^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
}
        