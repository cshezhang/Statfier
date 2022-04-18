

class MyClass {
  public static int good_static = 2; // OK
  public static int Bad_Static = 2; // violation, name 'Bad_Static'
                                    // must match pattern '^[a-z](_?[a-zA-Z0-9]+)*$'
  private static int Good_Static1 = 2; // OK
  static int Good_Static2 = 2; // OK
}
        