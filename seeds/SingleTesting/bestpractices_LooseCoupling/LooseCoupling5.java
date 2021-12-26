
import java.util.HashSet;
public class Foo {
    HashSet fooSet = new HashSet(); // NOT OK
    HashSet foo() { // NOT OK
        return fooSet;
    }
}
        