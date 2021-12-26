
public class SomeJNIClass {
    public SomeJNIClass() {
        System.loadLibrary("nativelib");
    }

    static {
         System.loadLibrary("nativelib");
    }

    public void invalidCallsInMethod() throws SecurityException, NoSuchMethodException {
        System.loadLibrary("nativelib");
    }
}
        