
public class Foo {
    private Object[] x;
    Object[] getArr() {
        return this.x.clone();
    }
}
        