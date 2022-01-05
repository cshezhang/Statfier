
public class Foo {
    boolean bar(Object foo) {
        StringBuffer sb = new StringBuffer();
        return sb.toString().equals(foo);
    }
    boolean bar2(Object foo) {
        StringBuilder sb = new StringBuilder();
        return sb.toString().equals(foo);
    }
}
        