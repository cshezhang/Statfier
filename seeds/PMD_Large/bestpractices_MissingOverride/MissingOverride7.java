
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;
public class AnonClassExample {
    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bar();
            }

            public void bar() {

            }
        }).start();
    }
}
        