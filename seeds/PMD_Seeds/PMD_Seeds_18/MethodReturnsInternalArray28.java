
import java.util.concurrent.Callable;

public class MyClass {
    private static final String[] FOO_BAR = new String[] { "foo", "bar" };
    private final Callable<String[]> returnsFooBar = new Callable<String[]>() {
        @Override
        public String[] call() {
            return FOO_BAR;
        }
    };
    
    private static String[] fooBarNonFinal = new String[] { "foo", "bar" };
    private final Callable<String[]> returnsFooBarNonFinal = new Callable<String[]>() {
        @Override
        public String[] call() {
            return fooBarNonFinal;
        }
    };
}
        