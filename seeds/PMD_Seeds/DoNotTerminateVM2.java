package iter0;

public class Bar {
    public void foo() {
        // NEVER DO THIS IN A APP SERVER !!!
        Runtime.getRuntime().exit(0);
        Runtime.getRuntime().halt(0);
    }
}
        