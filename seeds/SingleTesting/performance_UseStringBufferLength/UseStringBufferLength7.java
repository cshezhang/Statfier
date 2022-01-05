
public class Foo {
    boolean bar() {
        StringBuffer sb = new StringBuffer();
        return sb.toString().equals(baz(""));
    }
    String baz(String s) {
        return s + "x";
    }
}
        