
import net.sourceforge.pmd.lang.java.rule.multithreading.AvoidThreadGroupTest.ThreadGroup;

public class Foo {
    void bar() {
        ThreadGroup t = new ThreadGroup();
    }
}
        