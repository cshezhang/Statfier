
import java.util.Map;

class Foo {
    void foo(Map<String, String> map, String name, int[] arr) {
        Integer index = map.get(name);
        arr[index] = 4;
    }

}
        