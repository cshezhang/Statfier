
import java.util.List;

public class Foo {
    public static boolean bar(List lst, boolean b) {
        if(lst.size() == 0 && b){
            return true;
        }
        return false;
    }
}
        