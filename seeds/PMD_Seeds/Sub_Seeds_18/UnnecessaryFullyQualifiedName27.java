
import net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryFullyQualifiedNameTest.PhonyMockito;
import static net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryFullyQualifiedNameTest.PhonyMockito.*;
import static net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryFullyQualifiedNameTest.PhonyPowerMockito.mock;

public class Foo {
    private Bar bar = Mockito.mock(Bar.class); // doing simply mock(Bar.class) would use a differen method than intended
}
        