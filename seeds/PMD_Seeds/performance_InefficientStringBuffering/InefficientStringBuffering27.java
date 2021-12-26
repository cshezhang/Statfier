
public class Foo {

    private static final String STATIC_FINAL_F = "static_final_field";
    private final String FINAL_F = "final_field";

    private void good(String arg) {
        StringBuilder sb = new StringBuilder();
        sb.append("arg='").append(arg).append("'");
    }

    public void testAddInSecondOrThirdArgAppendGood() {
        StringBuilder wrappedLine = new StringBuilder();
        String str = "bar";
        int offset = 1;
        int wrapLength = 2;
        wrappedLine.append(str, offset + 1, wrapLength + offset + 1); // + but no string concat
    }

    private String testLiteralsGood(String arg) {
        StringBuilder sb = new StringBuilder();
        sb.append("lit:" + "etc");
        return sb.toString();
    }

    private String testFinalFieldGood() { // final assumed a constant expression
        return new StringBuilder().append("fld:" + FINAL_F).toString(); //good
    }

    private String testStaticFinalFieldGood() {
        return new StringBuilder().append("fld:" + STATIC_FINAL_F).toString(); // good
    }

    private String testFinalLocalGood() {
        final String local = "local"; // final assumed a constant expression
        return new StringBuilder().append("fld:" + local).toString(); // good
    }

    private String testFinalLocalGood2() {
        final String local = "local"; // final assumed a constant expression
        StringBuilder sb = new StringBuilder();
        sb.append("local:" + local); // good
        return sb.toString;
    }
}
        