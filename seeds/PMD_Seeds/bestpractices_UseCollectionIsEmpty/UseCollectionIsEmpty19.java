
import java.util.List;

public class Foo {
    void myMethod() {
        Object anonymous = new Object() {
            public boolean check(List lst) {
                if (lst.size() == 0) {
                    return true;
                }
                return false;
            }
        };
        class Local {
            public boolean check2(List lst) {
                return lst.size() == 0;
            }
        }
    }
    class Inner {
        public static boolean bar(List lst) {
            if(lst.size() == 0){
                return true;
            }
            return false;
        }
    }
}
        