
public class Foo {
    public int foo(int x) {
        try {
            x += 2;
            return x;
        } finally {
            int y;
        }
    }
}
        