
public class StringBufferTest {
    private static String TAG = "TAG";
    public void test() {
         final StringBuffer stringBuffer2 = new StringBuffer();
        // the following 2 lines are reported as ConsecutiveAppendsShouldReuse
        stringBuffer2.append("agrego ");
        stringBuffer2.append("un ");
        stringBuffer2.append("string ");    // but not on this one
        Log.i(TAG, stringBuffer2.toString());
    }
}
        