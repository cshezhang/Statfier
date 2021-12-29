
public class Foo {
    public void bar(String x) {
        if (x.indexOf("\n") == -1) {}
        if (x.indexOf("\t") == -1) {}
        if (x.indexOf("\b") == -1) {}
        if (x.indexOf("\r") == -1) {}
        if (x.indexOf("\f") == -1) {}
        if (x.indexOf("\\") == -1) {}
        if (x.indexOf("\'") == -1) {}
        if (x.indexOf("\"") == -1) {}
    }
}
        