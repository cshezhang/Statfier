
import java.util.Locale;

public class Test {

    public static void main(String args[]) {
        Test t = new Test();
        t.baz();
    }

    // Here we call both foo() and bar()
    public void baz() {
        foo().toLowerCase(Locale.US);
        bar().toLowerCase();
    }

    private String foo() {
        return "Hello World";
    }

    private String bar() {
        return "Hello World";
    }
}
        