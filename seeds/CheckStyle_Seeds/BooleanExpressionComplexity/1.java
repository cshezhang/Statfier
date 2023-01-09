

public class Test
{
  public static void main(String ... args)
  {
    boolean a = true;
    boolean b = false;

    boolean c = (a & b) | (b ^ a) | (a ^ b);   // OK, 1(&) + 1(|) + 1(^) + 1(|) + 1(^) = 5

    boolean d = (a | b) ^ (a | b) ^ (a || b) & b; // violation,
                                                 // 1(|) + 1(^) + 1(|) + 1(^) + 1(||) + 1(&) = 6
  }
}
        