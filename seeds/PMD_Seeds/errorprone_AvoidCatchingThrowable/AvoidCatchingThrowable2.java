
public class Foo {
    public void bar() {
        try {
            Foo.class.isAssignableFrom(null);
        } catch(java.lang.Throwable e) {
            e.printStackTrace();
        }
    }
}
        