
import java.util.Arrays;
public class Foo {
    void bar() {
        String foo = "foo";
        boolean b =  Arrays.toString(foo.toCharArray()).trim().isEmpty();
        int i = 2;
        b =  String.valueOf(i).trim().isEmpty();
    }
}
        