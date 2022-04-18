public class DL_SYNCHRONIZATION_ON_SHARED_CONSTANT {
    private static String LOCK = "LOCK";
    public void foo() {    
        synchronized(LOCK) {}
    }
}