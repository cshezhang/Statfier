

import edu.umd.cs.findbugs.annotations.DesireWarning;

public class Ideas_2011_07_22 {

  @DesireWarning("NP_NULL_ON_SOME_PATH")
  public int getHashCode0(Object x) {
    boolean b = x != null;
    if (b) System.out.println("Good");
    return x.hashCode();
  }
}

