
import java.util.*;

public class ConcatInLoop {

    private String logStatement = "";

    public void bad() {
        List<String> values = Arrays.asList("tic", "tac", "toe");
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
        