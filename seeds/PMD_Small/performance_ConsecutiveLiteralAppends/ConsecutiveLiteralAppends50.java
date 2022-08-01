
public class Foo {
    public String foo(final String value)
    {
        StringBuilder s = new StringBuilder("start:").append(value).append(":end");

        return s.toString();
    }
}
        