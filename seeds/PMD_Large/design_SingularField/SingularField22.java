
public class Foo {
    private class Bar {
        private int bar;

        private Bar(int barry) {
            bar = barry;
        }
    }

    public int foo() {
        Bar bart = new Bar(5);
        return bart.bar;
    }
}
        