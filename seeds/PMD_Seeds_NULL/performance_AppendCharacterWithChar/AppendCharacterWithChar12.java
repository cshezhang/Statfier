
public class Foo {
    public void bar(StringBuffer sb) {
        sb.append("\12");
        sb.append("\123");
    }
}
        