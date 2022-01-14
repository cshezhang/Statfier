
class A {
  public static void main(String[] args) {
    vararg(null);  // Noncompliant, prints "null"
    int[] arr = {1,2,3};
    vararg(arr);  // Noncompliant, prints "length: 1"
  }

  static void vararg(Object... s) {
    if (s == null) {
      System.out.println("null");
    } else {
      System.out.println("length: " + s.length);
    }
  }
}
