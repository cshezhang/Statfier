
public class Foo {
    public String value() {
        return "test";
    }

    public void bar() {
        Person p = new Person();
        int val = p.value();
        if (val == 0) {
            throw new RuntimeException();
        }
    }
}
        