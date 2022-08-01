
import net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryFullyQualifiedNameTest.ENUM2;
import static net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryFullyQualifiedNameTest.ENUM1.*;
import static net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryFullyQualifiedNameTest.ENUM2.*;

public class UnnecessaryFullyQualifiedName {
    public static void main(String[] args) {
        System.out.println(A);
        System.out.println(D);
        System.out.println(ENUM2.values());
    }
}
        