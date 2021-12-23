
import java.util.Iterator;
class Foo {
    void loop() {
        Iterable<E> it;
        Iterable<E> other;
        for (Iterator<E> iterator = it.iterator(), otherIterator = other.iterator(); iterator.hasNext();) {
            E item = iterator.next();
            E otherItem = otherIterator.next();
            doStuff();
        }
    }
}
        