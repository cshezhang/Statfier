
import java.util.ArrayList;

public class Foo {
    public static void main(String[] args) {
        var theList = new ArrayList<String>();
        if (theList.size() == 0) throw new IllegalArgumentException("empty list");
        if (theList.isEmpty()) throw new IllegalArgumentException("empty list");
    }
}
        