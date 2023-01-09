
package net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports;

import static net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports.PackagePrivateUtils.*;
import static net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports.PublicUtils.*;

public class Imports {
    int importtest() {
        int i = 0;
        i = f1(i);
        i = g1(i);
        i = f2(i);
        i = g2(i);
        i = f3(i);
        i = g3(i);
        return i;
    }
}
        