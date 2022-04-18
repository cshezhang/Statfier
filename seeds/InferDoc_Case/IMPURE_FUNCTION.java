import java.util.ArrayList;
import java.util.Iterator;

public class IMPURE_FUNCTION {
    static class Foo {
        public int x;
    }
    void makeAllZero_impure(ArrayList<Foo> list) {
        Iterator<Foo> listIterator = list.iterator();
        while (listIterator.hasNext()) {
            Foo foo = listIterator.next();
            foo.x = 0;
        }
    }
}