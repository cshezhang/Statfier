
import net.sourceforge.pmd.lang.java.rule.multithreading.DontCallThreadRunTest.TestThread;

public class Foo {
    public void bar() {
        new TestThread().run();
    }
}
        