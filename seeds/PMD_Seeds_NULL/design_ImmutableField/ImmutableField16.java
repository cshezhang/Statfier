
public class Foo {
    private int counter;

    private Foo() {
        counter = 0;
    }

    private int foo() {
        if (++this.counter < 3) {
            return 0;
        }
        return 1;
    }
}
        