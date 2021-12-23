package edu.polyu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static edu.polyu.Util.AST_TESTING;
import static edu.polyu.Util.CHECKSTYLE_MUTATION;
import static edu.polyu.Util.CHECKSTYLE_SEED_PATH;
import static edu.polyu.Util.ERRORPRONE_MUTATION;
import static edu.polyu.Util.ERRORPRONE_SEED_PATH;
import static edu.polyu.Util.GUIDED_RANDOM_TESTING;
import static edu.polyu.Util.MAIN_EXECUTION;
import static edu.polyu.Util.PMD_MUTATION;
import static edu.polyu.Util.PMD_SEED_PATH;
import static edu.polyu.Util.PURE_RANDOM_TESTING;
import static edu.polyu.Util.SINGLE_TESTING;
import static edu.polyu.Util.SINGLE_TESTING_PATH;
import static edu.polyu.Util.SONARQUBE_MUTATION;
import static edu.polyu.Util.SONARQUBE_SEED_PATH;
import static edu.polyu.Util.SPOTBUGS_MUTATION;
import static edu.polyu.Util.SPOTBUGS_SEED_PATH;
import static edu.polyu.Util.compactIssues;
import static edu.polyu.Util.initEnv;
import static edu.polyu.Util.startTimeStamp;

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
        try {
            if (SINGLE_TESTING) {
                if (PURE_RANDOM_TESTING) {
                    tester.pureRandomTesting(SINGLE_TESTING_PATH);
                }
                if (GUIDED_RANDOM_TESTING) {
                    tester.guidedRandomTesting(SINGLE_TESTING_PATH);
                }
                if (MAIN_EXECUTION) {
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
                if (SONARQUBE_MUTATION) {
                    targetSeedPath = SONARQUBE_SEED_PATH;
                }
                if (ERRORPRONE_MUTATION) {
                    targetSeedPath = ERRORPRONE_SEED_PATH;
                }
                if (CHECKSTYLE_MUTATION) {
                    targetSeedPath = CHECKSTYLE_SEED_PATH;
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
            int rules = 0;
            for(Map.Entry<String, HashMap<String, ArrayList<String>>> entry : compactIssues.entrySet()) {
                rules++;
                System.out.println(entry.getKey() + " " + entry.getValue().keySet().size());
            }
            System.out.println("Rule Number: " + rules);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long executionTime = System.currentTimeMillis() - startTimeStamp;
            System.out.println(String.format(
                    "Overall Execution Time is: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(executionTime),
                        TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))))
            );
        }
    }

}
