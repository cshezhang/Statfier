
import java.util.ArrayList;
import java.util.List;

public class UseArraysAsListFN {
    public List<String> convert(String[] arr) {
        List<String> result = new ArrayList<>();
        for (String s : arr) {
            result.add(s);  // violation expected
        }
        return result;
    }
}
        