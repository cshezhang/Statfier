
public class Foo {
    void bar() {
        MyClass myClass = null;
        try {
            myClass = new MyClass();
        } catch (Exception e) {
        } finally {
            myClass.cleanup(); // should be ok, it's closed with cleanup
        }
    }
}
        