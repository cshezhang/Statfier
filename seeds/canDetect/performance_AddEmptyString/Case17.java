package MutatorTestingCases;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021/12/20 4:54 PM
 */
public class Case17 {

    // https://pmd.github.io/latest/pmd_rules_java_performance.html#addemptystring
    public void foo() {
        String s = "" + 123;
    }
}
