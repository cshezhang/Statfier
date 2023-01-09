
public class Foo {
    private void baz(StringBuffer s, char[] chars, int start, int end) {
        s.append(chars, start, start - end);
    }
}
        