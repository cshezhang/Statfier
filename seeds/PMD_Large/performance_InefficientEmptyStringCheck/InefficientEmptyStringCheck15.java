
package net.sourceforge.pmd.lang.java.rule.performance.inefficientemptystringcheck;
public class StringTrimLength {
    String get() {
        return "foo";
    }
    void bar() {
        if (get().trim().length() == 0) {
            // violation missing
        }
        if (this.get().trim().length() == 0) {
            // violation missing
        }

        String bar = get();
        if (bar.trim().length() == 0) {
            // violation already detected
        }
        if (bar.toString().trim().length() == 0) {
            // violation missing
        }
    }
}
        