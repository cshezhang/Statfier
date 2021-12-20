package iter0;

import java.util.*;
import java.util.List;

public class ConcatInLoop {

    private String logStatement = "";

    public void bad() {
        java.util.List<String> values = Arrays.asList("tic", "tac", "toe");
        for (String val : values) {
            logStatement = logStatement + val + ", "; // bad
        }
    }

    public void good() {
        List<String> values = Arrays.asList("tic", "tac", "toe");
        StringBuilder sb = new StringBuilder(logStatement);
        for (String val : values) {
            sb.append(val).append(", ");
        }
        logStatement = sb.toString();
    }
}
        