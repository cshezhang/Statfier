
package net.sourceforge.pmd.lang.java.rule.errorprone.compareobjectswithequals;

import java.util.Date;

public class CompareObjectsWithEqualsSample {
    void bar(Date aDate, int aInt, Integer realInt) {
        if ( aDate == new Date( 0 ) ) {} // not ok
        if ( aInt == new Integer(1) ) {} // that's actually ok, since Integer will be unboxed
        if ( realInt == new Integer(1) ) {} // not ok
    }
}
        