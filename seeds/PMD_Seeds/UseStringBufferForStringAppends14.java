package iter0;

import java.util.*;
import java.util.List;

public class ConcatInLoop {
    public void bad1() {
        StringBuilder logStatement = new StringBuilder();
        List<String> values = Arrays.asList("tic", "tac", "toe");
        for (String val : values) {
            logStatement.append(val + ", "); // bad, but that's InefficientStringBuffering
        }
    }
}
        