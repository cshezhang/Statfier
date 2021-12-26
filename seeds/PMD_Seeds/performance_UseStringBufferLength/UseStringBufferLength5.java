
public class Foo {
    boolean bar() {
        StringBuffer sb = new StringBuffer();
        return sb.toString().equals("" + "x");
    }
}
        