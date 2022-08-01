
public class Foo {
    public void bar(int a, final Object o) {
        int z = a;
        Object x = o.clone();
    }
}
        