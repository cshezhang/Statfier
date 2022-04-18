
import net.sourceforge.pmd.lang.java.rule.bestpractices.switchstmtsshouldhavedefault.SimpleEnum;

public class Foo {
    void bar() {
        SimpleEnum x;

        // This switch is exhaustive
        switch (x) {
        case BZAZ:
            int y = 8;
            break;
        case FOO:
            break;
        case BAR:
            int w = 8;
            break;
        }
    }
}
        