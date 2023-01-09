
import java.util.concurrent.Callable;

public class UnnecessaryLocal {
    void foo() {
        Callable<String> c = () -> { String s = "1"; return s; };
    }
}
        