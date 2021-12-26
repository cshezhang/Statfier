
import java.util.*;

public class ConcatInLoop {
    public void good() {
        StringBuilder logStatement = new StringBuilder();
        List<String> values = Arrays.asList("tic", "tac", "toe");
        for (String val1 : values) {
            logStatement.append(val1);
        }
        for (String val2 : values) logStatement.append(val2);
    }
}
        