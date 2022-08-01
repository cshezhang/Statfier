
public class Foo {
    void bar() {
        Collection c = new ArrayList();
        c.add(new Integer(1));
        Integer[] a = (Integer [])c.toArray(new Integer[c.size()]);;
    }
}
        