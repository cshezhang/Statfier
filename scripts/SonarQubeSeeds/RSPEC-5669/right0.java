
class A {
  public static void main(String[] args) {
    vararg((Object) null); // prints 1
    Object[] arr = {1,2,3};
    vararg(arr); // prints 3
  }

  static void vararg(Object... s) {
    if (s == null) {
      System.out.println("null"); // not reached
    } else {
      System.out.println("length: " + s.length);
    }
  }
}
