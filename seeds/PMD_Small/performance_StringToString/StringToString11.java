
public class Foo {
    public Integer value() {
        return 0;
    }

    public String bar() {
        Object obj = new Object() {
            public String value() {
                return "0";
            }
        };
        return value().toString();
    }
}
        