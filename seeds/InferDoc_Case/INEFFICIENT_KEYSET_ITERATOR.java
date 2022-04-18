import java.util.HashMap;
import java.util.Map;

public class INEFFICIENT_KEYSET_ITERATOR {

    void foo(String key, Integer value) {}

    void inefficient_loop_bad(HashMap<String, Integer> testMap) {
        for (String key : testMap.keySet()) {
            Integer value = testMap.get(key); // extra look-up cost
            foo(key, value);
        }
    }

    void efficient_loop_ok(HashMap<String, Integer> testMap) {
        for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            foo(key, value);
        }
    }
}