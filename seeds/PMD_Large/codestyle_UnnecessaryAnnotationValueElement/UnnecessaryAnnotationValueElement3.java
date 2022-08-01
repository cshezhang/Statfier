
public class Foo {
    private String y;

    @TestMethodAnnotation(value = "TEST", other = "TEST1")
    public void bar() {
        int x = 42;
        return;
    }
}
        