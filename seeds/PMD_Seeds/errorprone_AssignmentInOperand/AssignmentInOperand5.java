
public class Foo {
    public void bar() {
        for (int i = 0; (i = i + 1) < 10; i++) {
            int x = i;
        }
    }
}
        