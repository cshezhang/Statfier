import edu.umd.cs.findbugs.annotations.DesireWarning;

public class ArrayToString1 {

  public static void main(String[] args) {
    ArrayToString1 a = new ArrayToString1();
    a.print0(args);
  }

  @DesireWarning("USELESS_STRING")
  public void print0(String args[]) {
    System.out.println(args.toString());
  }
}

