
public class Foo {
    public String value(String str) {
        return str.trim();
    }

    public Integer value(Integer num) {
        return num + 1;
    }

    public void bar() {
        String str = value(5).toString();
    }
}
        