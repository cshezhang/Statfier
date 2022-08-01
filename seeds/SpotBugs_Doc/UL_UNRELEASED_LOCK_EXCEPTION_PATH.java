public class UL_UNRELEASED_LOCK_EXCEPTION_PATH {
    public void foo() {
        Lock l = new Lock();
        l.lock();
        try {
            // do something
        } finally {
            l.unlock();
        }
    }
}