
import java.util.List;
import java.util.ArrayList;

public class Foo {

    private List<String> list = new ArrayList<>();
    private Bar notACollection = new Bar();

    public void bar() {
        if (this.list.size() == 0) {
            throw new RuntimeException("Empty list");
        }
        if (notACollection.size() == 0) { }
        if (this.notACollection.size() == 0) { }
    }

    public int size() {
        return 0;
    }
}
        