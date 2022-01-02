package edu.polyu;

import edu.polyu.util.TriTuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static edu.polyu.ASTWrapper.invalidSeed;
import static edu.polyu.ASTWrapper.validSeed;
import static edu.polyu.Util.AST_TESTING;
import static edu.polyu.Util.AST_TESTING_PATH;
import static edu.polyu.Util.GUIDED_RANDOM_TESTING;
import static edu.polyu.Util.MAIN_EXECUTION;
import static edu.polyu.Util.PURE_RANDOM_TESTING;
import static edu.polyu.Util.PURE_TESTING;
import static edu.polyu.Util.SINGLE_TESTING_PATH;
import static edu.polyu.Util.compactIssues;
import static edu.polyu.Util.initEnv;
import static edu.polyu.Util.sourceSeedPath;
import static edu.polyu.Util.startTimeStamp;

/**
 * Description: Mutator Scheduler
 * Author: Vanguard
 * Date: 2021-08-08 16:20
 */
public class AutomaticTester {

    public static void main(String[] args) {
        initEnv();
        Schedule tester = Schedule.getInstance();
        if(AST_TESTING) {
            tester.testAST(AST_TESTING_PATH);
            System.exit(0);
        }
        if(PURE_TESTING) {
            tester.pureTesting(SINGLE_TESTING_PATH);
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
            int seqCount = 0;
            int allValidMutantNumber = 0;
            for(Map.Entry<String, HashMap<String, ArrayList<TriTuple>>> entry : compactIssues.entrySet()) {
                HashMap<String, ArrayList<TriTuple>> seq2mutants = entry.getValue();
                System.out.println("Rule: " + entry.getKey() + " Seq Number: " + seq2mutants.size());
                seqCount += seq2mutants.size();
                for(Map.Entry<String, ArrayList<TriTuple>> subEntry : seq2mutants.entrySet()) {
                    System.out.println("Transoformation Sequence: " + subEntry.getKey());
                    for(TriTuple triTuple : subEntry.getValue()) {
                        System.out.println(triTuple);
                    }
                    allValidMutantNumber += subEntry.getValue().size();
                }
            }
            System.out.println("Rule Number: " + rules);
            System.out.println("Unique Sequence: " + seqCount);
            System.out.println("Valid Mutant Number: " + allValidMutantNumber);
            System.out.println("Invalid Seed Number:" + invalidSeed);
            System.out.println("Valid Seed Number:" + validSeed);
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