package iter0;

import java.util.*;
import java.util.List;

public class ConcatInLoop {

    public void bad1() {
        String logStatement = "";
        java.util.List<String> values = Arrays.asList("tic", "tac", "toe");
        for (String val : values) {
            logStatement = logStatement + val + ", "; // bad
        }
        Iterator iter = values.iterator();
        while (iter.hasNext()) {
            logStatement = logStatement + iter.next() + ", "; // bad
        }
    }
    public void bad2() {
        String log = "";
        java.util.List<String> values = Arrays.asList("tic", "tac", "toe");
        for (String val1 : values) {
            log += val1; // bad
        }
        for (String val2 : values) log += val2; // bad
    }

    public void bad3() {
        String logStatement = "";
        List<String> values = Arrays.asList("tic", "tac", "toe");
        for (String val : values) {
            logStatement += val + ", "; // bad
        }
    }
}
        