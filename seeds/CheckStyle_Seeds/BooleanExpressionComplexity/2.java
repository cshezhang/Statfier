

public class Test
{
  public static void main(String ... args)
  {
    boolean a = true;
    boolean b = false;

    boolean c = (!a && b) | (a || !b) ^ a;    // OK, 1(&&) + 1(||) + 1(^) = 3
                                              // | is ignored here

    boolean d = a ^ (a || b) ^ (b || a) & a; // violation, 1(^) + 1(||) + 1(^) + 1(||) = 4
                                             // & is ignored here
  }
}
        