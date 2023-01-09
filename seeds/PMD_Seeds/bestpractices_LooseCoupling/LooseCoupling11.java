
import java.util.HashSet;
public class Foo {
    void firstMethod() { }
    void myMethod() {
        class Inner {
            HashSet foo() {
                return new HashSet();
            }
        }
        Object o = new Object() {
            HashSet foo() { return new HashSet(); }
        };
    }
    class Nested {
        HashSet foo() {
            return new HashSet();
        }
    }
}
        