package MutatorTestingCases;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021/12/20 5:14 下午
 */
public class Case19 {

    public void foo() {
        // https://pmd.github.io/latest/pmd_rules_java_performance.html#usestringbufferlength
        StringBuffer sb = new StringBuffer();
        if (sb.toString().equals("")) {}        // inefficient
    }
}
