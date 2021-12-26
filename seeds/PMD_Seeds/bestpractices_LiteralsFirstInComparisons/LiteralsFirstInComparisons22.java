
public class Foo {
    private String getStr(String a) {
        return "a" + a;
    }
    public void bar() {
        if (getStr("b").equals("ab")) { }   // nok
        if ("ab".equals(getStr("b"))) { }   // ok
    }
}
        