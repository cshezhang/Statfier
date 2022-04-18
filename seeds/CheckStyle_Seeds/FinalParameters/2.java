

public class Point {
  public Point() { } // ok
  public Point(final int m) { } // ok
  public Point(final int m,int n) { } // ok
  public void methodOne(final int x) { } // ok
  public void methodTwo(int x) { } // ok
  public static void main(String[] args) { } // violation, args should be final
}
        