package iter0;

public class Foo1 {
    public class InnerClass {
        private InnerClass() {
        }
    }
    void method() {
        new InnerClass(); //Causes generation of accessor
    }
}
        