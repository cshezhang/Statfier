
import java.util.concurrent.Callable;

public class UnnecessaryLocal {
    void foo() {
        Callable<String> c = new Callable<>() {
            public String call() {
                String s = "1";
                return s;
            }
        };
    }
}
        