
import java.util.ArrayList;
import java.util.List;

public class Test {
    public void foo() {
        String [] result = new String[10000];
        // some code which populates result

        // copy n items from result to a List, such that resultList[i] = result[i+1]
        List<String> resultList = new ArrayList<String>();
        for (int i = 1; i <= n; i++) {
            resultList.add(result[i]);
        }
    }
}
        