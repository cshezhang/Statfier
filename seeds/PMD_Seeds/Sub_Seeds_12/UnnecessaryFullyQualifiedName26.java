
import net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryFullyQualifiedNameTest.PhonyMockito;
import static net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryFullyQualifiedNameTest.PhonyMockito.*;
import static net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryFullyQualifiedNameTest.PhonyPowerMockito.*;

public class Foo {
    private Bar bar = Mockito.mock(Bar.class); // doing simply mock(Bar.class) is ambiguous (compile error)
}
        