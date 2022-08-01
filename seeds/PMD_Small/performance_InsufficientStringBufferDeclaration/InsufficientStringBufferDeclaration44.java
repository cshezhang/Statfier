
public final class test {

    private test() {}

    public static void main(final String ... args) {
        final String NEWLINE = "\n";
        StringBuilder report = new StringBuilder(args.length > 1 ? 100 : 200);
        report.append(
            "### Testing report" + NEWLINE +
            "# Testing" + NEWLINE +
            "# More Contents" + NEWLINE);
    }
}
        