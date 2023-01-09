
public class StringBufferTest {
    public void test() {
        final StringBuffer stringBuffer = new StringBuffer().append("Added ").append(" a ");
        stringBuffer.append("string longer than 16 characters");

        // reassignment with chaining
        stringBuffer = new StringBuffer().append("Added ").append(" a ");
        stringBuffer.append("string longer than 16 characters and longer");
    }
}
        