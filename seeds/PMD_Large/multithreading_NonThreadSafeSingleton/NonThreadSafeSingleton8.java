
public class A extends B {
    static B instance = null;
    private boolean bar = false;

    static void foo() {
        if (instance == null ) {
            instance = new A();
            ((A)instance).bar=false;
        }
    }
}
        