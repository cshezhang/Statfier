
public class Foo {

    private static final String STATIC_FINAL_F = "static_final_field";
    private static String static_f = "static_field";
    private final String FINAL_F = "final_field";

    private void concatIsBad(String arg) {
        StringBuilder sb = new StringBuilder();
        sb.append("arg='" + arg + "'"); // bad
    }

    private void concatIsBad2(String arg) {
        StringBuilder sb = new StringBuilder().append("arg='" + arg + "'"); // bad
    }

    private void concatIsBad3(String arg) {
        StringBuilder sb;
        sb = new StringBuilder().append("arg='" + arg + "'"); // bad
    }

    private void concatNumeric() {
        StringBuilder sb = new StringBuilder();
        sb.append(1 + 2);
    }

    private String testStaticBad() {
        StringBuilder sb = new StringBuilder().append("fld:" + static_f); // bad
        return sb.toString();
    }

    private String testFinalLocalBad() {
        String local = "local"; // non-final, jdk9+ optimized with indified Strings, assumed still worse
        return new StringBuilder().append("fld:" + local).toString(); // assumed bad
    }
}
        