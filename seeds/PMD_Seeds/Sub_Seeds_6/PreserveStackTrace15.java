
public class Foo {
    public void bar() {
        try {
        } catch (Exception notUsed) {
            RuntimeException ex = new RuntimeException();
            throw ex;
        }
    }
}
        