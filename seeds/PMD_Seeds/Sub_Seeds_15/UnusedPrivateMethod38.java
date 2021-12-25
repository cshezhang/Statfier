
public class Foo {
    public void bar() {
        int i = 1;
        String s = getBoolString(i > 0);
    }
    private String getBoolString(boolean b) {
        return b ? "TRUE" : "FALSE";
    }
}
        