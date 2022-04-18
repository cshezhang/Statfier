public class UL_UNRELEASED_LOCK {
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