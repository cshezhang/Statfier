

public class Test
{
  public static void main(String ... args)
  {
    boolean a = true;
    boolean b = false;

    boolean c = (a & b) | (b ^ a);       // OK, 1(&) + 1(|) + 1(^) = 3 (max allowed 3)

    boolean d = (a & b) ^ (a || b) | a;  // violation, 1(&) + 1(^) + 1(||) + 1(|) = 4
  }
}
        