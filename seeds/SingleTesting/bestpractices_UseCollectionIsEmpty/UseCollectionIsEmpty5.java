
import java.util.List;

public class Foo {
    public static boolean bar(List lst, boolean b) {
        if(lst.isEmpty() && b){
            return true;
        }
        return false;
    }
}
        