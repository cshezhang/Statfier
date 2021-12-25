
public class Foo {
    private static Runnable staticMethod() {
        return () -> System.out.println("run");
    }
    private Runnable doLater() {
        return () -> System.out.println("later");
    }
    private Runnable doLater2() {
        return () -> System.out.println("later");
    }

    public static void main(String[] args) {
        Runnable r1 = Foo::staticMethod;
        Runnable r2 = new Foo()::doLater;
    }
    public void doNow() {
        Runnable r3 = this::doLater2;
        r3.run();
    }
}
        