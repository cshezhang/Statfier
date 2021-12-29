
class Foo {
    private int value;

    public int setValue(int value) {
        this.value = value;
        return value;
    }
}
public class Bar extends Foo {
    private int value;

    @Override
    public int setValue(int value) {
        this.value = value;
        return value;
    }
}
        