import java.util.ArrayList;
import java.util.List;

public class UseDiamondOperatorFalseNegative {
  public void test() {
    final List<String> l2;
    l2 =
        true
            ? new ArrayList<String>()
            : new ArrayList<String>(); // FN twice for java8+, but for java7, this is ok!
  }
}

