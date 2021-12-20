package MutatorTestingCases;

/**
 * Description:
 * Author: Austin Zhang
 * Date: 2021/12/20 4:53 下午
 */
public class Case16 {

    // https://github.com/spotbugs/spotbugs/blob/51e586bed98393e53559a38c1f9bd15f54514efa/spotbugsTestCases/src/java/sfBugs/Bug2912638.java
    public boolean test1a(String s) {
        return s == "test";
    }

}
