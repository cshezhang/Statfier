
import java.util.List;
import java.util.Collections;

public class Foo {
    // Not a good idea...
    public List<String> bar()
    {
        // ...
        return Collections.emptyList();
    }
}
        