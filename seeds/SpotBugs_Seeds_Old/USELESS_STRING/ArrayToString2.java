import edu.umd.cs.findbugs.annotations.DesireWarning;

public class ArrayToString2 {

  public static void main(String[] args) {
    ArrayToString2 a = new ArrayToString2();
    a.print1();
  }

  @DesireWarning("USELESS_STRING")
  public void print1() {
    String[] args2 = new String[] {"Hello", "there"};
    System.out.println(args2.toString());
  }
}

