
public class Foo {
    public String bar() {
        switch (sign) {
        case  1:
            return "+";
        case  0:
            return "0";
        case -1:
            return "-";
        default:
            throw new IllegalArgumentException();
        }
    }
}
        