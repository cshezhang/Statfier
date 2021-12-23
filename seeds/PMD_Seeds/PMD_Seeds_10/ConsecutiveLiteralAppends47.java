
public class StringBufferTest {

    public void test() {

        // This lint is reported as ConsecutiveLiteralAppends, but says ".append is called **5** consecutive times"
        final StringBuffer stringBuffer = new StringBuffer().append("agrego ").append("un ");
        stringBuffer.append("string "); // and in this line says ".append is called **4** consecutive times"
        Log.i(TAG, stringBuffer.toString());

        final StringBuffer stringBuffer2 = new StringBuffer();
        // ConsecutiveLiteralAppends is not reported on any of these lines
        stringBuffer2.append("agrego ");
        stringBuffer2.append("un ");
        stringBuffer2.append("string ");
        Log.i(TAG, stringBuffer2.toString());
    }
}
        