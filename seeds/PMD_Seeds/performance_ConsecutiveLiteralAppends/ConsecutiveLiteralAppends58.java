
public final class Test {
    public String foo() {
        final StringBuilder sb = new StringBuilder();
        sb.append("foo"); // violation here
        try {
            sb.append("bar");
            final String res = methodThatMightThrow();
            sb.append(res);
        } catch (IOException ioe) {
            // noop
        }
    }
}
        