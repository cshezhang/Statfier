
import java.util.List;

public class Foo {
    public static int modulo = 2;
    public static boolean bar(List lst) {
        if(lst.size() % modulo == 0){
            return true;
        }
        return false;
    }
}
        