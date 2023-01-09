
public class OuterClass {

    public void foo() {
        InnerClass.doSomething();
    }

    static class InnerClass {
        private static void doSomething() {}
    }
}
        