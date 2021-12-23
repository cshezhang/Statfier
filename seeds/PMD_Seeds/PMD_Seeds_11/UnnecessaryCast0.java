
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.uilt.HashMap;

public class UnnecessaryCastSample {
    private Map<Integer, String> map = new HashMap<>();

    public void localVars() {
        List<String> stringList = Arrays.asList("a");
        String element = (String) stringList.get(0);

        List<Double> doubleList = new ArrayList<>();
        Double number = (Double) doubleList.get(0);

        Map<String, String> stringMap = new HashMap<>();
        String mapData = (String) stringMap.get("a");
    }

    public void fields() {
        map.put(1, "test");
        String val = (String) map.get(1);
    }

    public void fields2() {
        this.map.put(1, "test");
        String val = (String) this.map.get(1);
    }
}
        