
import java.util.Map;

public class Foo {
    final Map map;
    public boolean bar(Foo other) {
        if (this.map.size() != other.map.size()){
            return true;
        }
        return false;
    }
}
        