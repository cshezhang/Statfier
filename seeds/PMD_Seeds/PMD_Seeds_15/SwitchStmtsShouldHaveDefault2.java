
import net.sourceforge.pmd.lang.java.rule.bestpractices.switchstmtsshouldhavedefault.SimpleEnum;

public class Foo {
    void bar() {
        SimpleEnum a;

        // This switch is NOT exhaustive
        switch (a) {
        case BZAZ:
            int y = 8;
            break;
        }
    }
}
        