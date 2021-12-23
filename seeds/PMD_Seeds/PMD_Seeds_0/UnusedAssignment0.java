
public class Foo {
    void bar(int b) {
        for (int i = 0; i < 10; i++) {
            throw new Exception();
        }
    }
}
        