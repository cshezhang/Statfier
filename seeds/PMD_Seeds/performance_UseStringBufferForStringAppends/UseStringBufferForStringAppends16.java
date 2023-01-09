
import java.util.*;

public class ConcatInLoop {
    public String bad() {
        String description = " " + ";";
        List<String> persons = new ArrayList<String>();
        for (final String person : persons) {
            if (person != null) {
                description += "0" + ":"; //bad
            } else {
                description += ":"; //bad
            }
            description += person.toString() + ":"; // bad
            description += ";"; // bad
            description += person.toString(); // bad
        }
        return description;
    }
}
        