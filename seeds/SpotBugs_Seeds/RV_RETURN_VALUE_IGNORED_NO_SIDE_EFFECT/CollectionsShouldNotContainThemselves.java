package gcUnrelatedTypes;

import edu.umd.cs.findbugs.annotations.ExpectWarning;
import java.util.HashSet;
import java.util.Set;

public class CollectionsShouldNotContainThemselves {

  @ExpectWarning("DMI")
  public static void main(String args[]) {

    Set s = new HashSet();

    s.contains(s);
    s.remove(s);
    s.containsAll(s);
    s.retainAll(s);
    s.removeAll(s);
  }
}

