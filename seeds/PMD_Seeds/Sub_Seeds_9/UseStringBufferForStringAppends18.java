
import java.util.*;

public class ConcatInLoop {

    public void bad() {
        String logStatement = "";
        List<String> values = Arrays.asList("tic", "tac", "toe");
        int i = 0;
        do {
            logStatement = logStatement + values.get(i++) + ", "; // bad
        } while (i < values.length());
    }
}
        