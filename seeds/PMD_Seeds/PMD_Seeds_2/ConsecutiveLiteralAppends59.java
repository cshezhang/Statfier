
public final class Test {
    public String foo() {
        final StringBuilder sb = new StringBuilder();
        Runnable r = () -> sb.append("foo");
        Runnable r2 = () -> sb.append("bar");

        r.run();
        System.out.println(sb.toString());
        r2.run();
        return sb.toString();
    }
}
        