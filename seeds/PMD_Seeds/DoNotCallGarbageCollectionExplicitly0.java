package iter0;

public class GCCall {
    public void doSomething() {
        // Explicit gc call !
        Runtime.getRuntime().gc();
    }
}
        