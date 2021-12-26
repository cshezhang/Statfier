
public class Foo {
    public void bar(String x) {
        if (x.indexOf("\12") == -1) {}
        if (x.indexOf("\123") == -1) {}
    }
}
        