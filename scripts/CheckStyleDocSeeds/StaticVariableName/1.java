

class MyClass {
  public static int GoodStatic1 = 2; // OK
  protected static int GoodStatic2 = 2; //OK
  private static int goodStatic = 2 // OK
  private static int BadStatic = 2; // violation, name 'BadStatic'
                                    // must match pattern '^[a-z][a-zA-Z0-9]*$'
}
        