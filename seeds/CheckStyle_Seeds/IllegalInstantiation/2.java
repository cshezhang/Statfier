

public class MyTest {
  public class Boolean {
    boolean a;

    public Boolean (boolean a) { this.a = a; }
  }

  public void myTest (boolean a, int b) {
    Boolean c = new Boolean(a); // violation, instantiation of
                                // java.lang.Boolean should be avoided
    java.lang.Boolean d = new java.lang.Boolean(a); // violation, instantiation of
                                                    // java.lang.Boolean should be avoided

    Integer e = new Integer(b); // violation, instantiation of
                                // java.lang.Integer should be avoided
    Integer f = Integer.valueOf(b); // OK
  }
}
        