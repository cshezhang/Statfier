
import java.util.Arrays;
public class Foo {
    void bar() {
        String foo = "foo";
        boolean b =  Arrays.toString(foo.toCharArray()).trim().isEmpty();
        b =  String.valueOf(2).trim().isEmpty();
    }
}
        