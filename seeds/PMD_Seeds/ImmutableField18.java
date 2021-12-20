package iter0;

public class MyClass {
    private int positive = 1;

    public MyClass(int value) {
        // if negative keep default
        if (value > 0) {
            positive = value;
        }
    }
}
        