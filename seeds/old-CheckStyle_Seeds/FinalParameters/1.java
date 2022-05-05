

public class Point {
  public Point() { } // ok
  public Point(final int m) { } // ok
  public Point(final int m,int n) { } // violation, n should be final
  public void methodOne(final int x) { } // ok
  public void methodTwo(int x) { } // ok
  public static void main(String[] args) { } // ok
}
        