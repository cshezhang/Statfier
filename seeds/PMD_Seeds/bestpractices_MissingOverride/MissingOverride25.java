
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

public enum EmptyEnum {
    ;

    public static void main(String... args) {
        method();
    }

    public static void method(int... a) {
        System.out.println("1");
    }

    @SuppressWarnings("PMD.AvoidUsingShortType")
    public static void method(short... b) {
        System.out.println("2");
    }
}
        