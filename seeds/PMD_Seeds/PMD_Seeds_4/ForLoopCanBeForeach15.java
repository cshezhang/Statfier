
import java.util.Iterator;

class Foo<T> implements Iterable<T> {

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    private void fofo() {
        for (Iterator<T> it = this.iterator(); it.hasNext();) {
            T item = it.next();
        }
    }
}
        