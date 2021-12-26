
import java.util.*;
public class Foo {
    Set fooSet = new HashSet(); // OK
    Set foo() {
        return fooSet;
    }
}
        