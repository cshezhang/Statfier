

class OurThreadUtils {
    static native boolean isMainThread();

    static native boolean isUiThread();

    static void assertMainThread() {
    }

    static void assertOnUiThread() {
    }

    static void assertOnBackgroundThread() {
    }

    static void assertHoldsLock(Object lock) {
    }
}
