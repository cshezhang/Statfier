
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("first");
        list.add("second");

        int notEmpty = 0;
        for (String string : list) {
                if (!string.isEmpty()) {
                    notEmpty++;
                }
        }

        System.out.println(list.size() + " (" + (notEmpty) + " not empty)");
    }
}
        