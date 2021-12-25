
import other.exceptions.SecurityException;
import yet.an.other.one.NoSuchMethodException;

public class MyClass {

    public void method() {
      // no args, no violation
    }

    public void otherMethod(String arg1, String arg2) {
      // less than 2, should be ok
    }

    public void otherMethod(String arg1, String arg2, int arg3) {
      // less than 2 Strings, should be ok
    }

    public void yetAnOtherOneWithException(String arg1,int arg2) throws SecurityException, NoSuchMethodException {
      System.loadLibrary("nativelib");
    }
}
        