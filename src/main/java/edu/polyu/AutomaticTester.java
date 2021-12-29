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
import static edu.polyu.Util.TriTuple;

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
            int rules = compactIssues.keySet().size();
            int allValidMutantNumber = 0;
            for(Map.Entry<String, HashMap<String, ArrayList<TriTuple>>> entry : compactIssues.entrySet()) {
                HashMap<String, ArrayList<TriTuple>> seq2mutants = entry.getValue();
                System.out.println("Rule: " + entry.getKey() + " Seq Number: " + seq2mutants.size());
                for(Map.Entry<String, ArrayList<TriTuple>> subEntry : seq2mutants.entrySet()) {
                    System.out.println("Transoformation Sequence: " + subEntry.getKey());
                    for(TriTuple triTuple : subEntry.getValue()) {
                        System.out.println(triTuple);
                    }
                    allValidMutantNumber += subEntry.getValue().size();
                }
            }
            System.out.println("Rule Number: " + rules);
            System.out.println("Valid Mutant Number: " + allValidMutantNumber);
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