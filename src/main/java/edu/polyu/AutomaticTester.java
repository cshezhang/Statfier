package edu.polyu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static edu.polyu.Util.AST_TESTING;
import static edu.polyu.Util.AST_TESTING_PATH;
import static edu.polyu.Util.GUIDED_RANDOM_TESTING;
import static edu.polyu.Util.MAIN_EXECUTION;
import static edu.polyu.Util.PURE_RANDOM_TESTING;
import static edu.polyu.Util.compactIssues;
import static edu.polyu.Util.initEnv;
import static edu.polyu.Util.sourceSeedPath;
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
            tester.testAST(AST_TESTING_PATH);
            System.exit(0);
        }
        try {
            if (PURE_RANDOM_TESTING) {
                tester.pureRandomTesting(sourceSeedPath);
            }
            if (GUIDED_RANDOM_TESTING) {
                tester.guidedRandomTesting(sourceSeedPath);
            }
            if (MAIN_EXECUTION) {
                tester.executeMutation(sourceSeedPath);
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
