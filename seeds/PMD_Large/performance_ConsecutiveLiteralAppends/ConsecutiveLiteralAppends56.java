
public class ConsecutiveLiteralAppendsFP {
    public void test() {
        StringBuilder sb = new StringBuilder(
                "foo\n" +
                "bar\n" +
                "baz\n");

        while (sb.length() < 100) {
            sb.append(" ");
        }
        System.out.println(sb.toString());
    }
}
        