
public class Foo {
    public void bar() {
        String s = getData(getLong()).toString();
    }
    public long getLong() {
        return 0;
    }
    public long getData(String d) {
        return 0;
    }
    public String getData(long d) {
        return "";
    }
}
        