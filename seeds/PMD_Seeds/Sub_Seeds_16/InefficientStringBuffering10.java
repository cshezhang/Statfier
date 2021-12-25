
public class Foo {
    public void bar(int i) {
        StringBuffer buf = new StringBuffer();
        buf.append(getFoo(getBar(i + "hi")));
    }
}
        