
public class Test {
    //Thats no Violation in PMD:
    public boolean compare1(final String aString) {
        return aString.equals("S");
    }

    //Thats a Violation in PMD:
    public boolean compare2(final String aString) {
        return "A".equals(aString);              // < - - false positive
    }

    // no violation, because the object is not used
    public Object create() {
        Object o = myFactory.create();
        return o;
    }

    // no violation, should be an exception, as the builder pattern is used here
    public void toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("string").append("string").append("string");
        return buffer.toString();
    }
}
        