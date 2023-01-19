public class Case6 {
  public static void test(Object o) {
    if (o == null) // After removing for loop
    new IllegalArgumentException("Forgot to throw this");
    if (o instanceof String) {
      if (o == null) System.out.println("This check is redundant");
      return;
    }
    System.out.println(o.hashCode());
  }
}

