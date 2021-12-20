package edu.polyu;

import java.util.concurrent.TimeUnit;
import static edu.polyu.Util.*;

/*
 * @Description: This class only contains Automatic Tester related functions, other modules have been moved to Util class.
 * @Author: Vanguard
 * @Date: 2021-12-20 14:20:55
 */
public class AutomaticTester {

    public static void main(String[] args) {
        initEnv();
        Schedule tester = Schedule.getInstance();
        if(AST_TESTING) {
            tester.testAST(SINGLE_TESTING_PATH);
            System.exit(0);
        }
        if(SINGLE_TESTING) {
            if(PURE_RANDOM_TESTING) {
                tester.pureRandomTesting(SINGLE_TESTING_PATH);
            }
            if(GUIDED_RANDOM_TESTING) {
                tester.guidedRandomTesting(SINGLE_TESTING_PATH);
            }
            if(MAIN_EXECUTION) {
                tester.executeMutation(SINGLE_TESTING_PATH);
            }
        } else {
            String targetSeedPath = null;
            if (PMD_MUTATION) {
                targetSeedPath = PMD_SEED_PATH;
            }
            if (SPOTBUGS_MUTATION) {
                targetSeedPath = SPOTBUGS_SEED_PATH;
            }
            if (PURE_RANDOM_TESTING) {
                tester.pureRandomTesting(targetSeedPath);
            }
            if (GUIDED_RANDOM_TESTING) {
                tester.guidedRandomTesting(targetSeedPath);
            }
            if (MAIN_EXECUTION) {
                tester.executeMutation(targetSeedPath);
            }
        }
        long executionTime = System.currentTimeMillis() - startTimeStamp;
        System.out.println(String.format(
                "Overall Execution Time is: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(executionTime),
                        TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))
                )));
    }

}
