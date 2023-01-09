
public class Foo {
    public static void testStringBufferSizeBug() {
        final String temp = "Hello world!";
        final StringBuffer sb = new StringBuffer(temp.length() * 2);
        sb.append(temp);
        sb.append("abc");
    }
    public static void testStringBufferSizeBug2() {
        final String temp = "Hello world!";
        final StringBuilder sb = new StringBuilder(temp.length() * 2);
        sb.append(temp);
        sb.append("abc");
    }
}
        