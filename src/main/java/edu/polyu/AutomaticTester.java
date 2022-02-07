package edu.polyu;

import edu.polyu.util.TriTuple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static edu.polyu.ASTWrapper.failMutation;
import static edu.polyu.ASTWrapper.invalidSeed;
import static edu.polyu.ASTWrapper.succMutation;
import static edu.polyu.ASTWrapper.validSeed;
import static edu.polyu.Util.*;

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
//                tester.guidedRandomTesting(sourceSeedPath);
                if(PMD_MUTATION) {
                    tester.executePMDMutation(sourceSeedPath);
                }
                if(SPOTBUGS_MUTATION) {
                    tester.executeSpotBugsMutation(sourceSeedPath);
                }
            }
            if (MAIN_EXECUTION) {
                if(PMD_MUTATION) {
                    tester.executePMDMutation(sourceSeedPath);
                }
                if(SPOTBUGS_MUTATION) {
                    tester.executeSpotBugsMutation(sourceSeedPath);
                }
                if(CHECKSTYLE_MUTATION) {
                    tester.executeCheckStyleMutation(sourceSeedPath);
                }
                if(INFER_MUTATION) {
                    tester.executeInferMutation(sourceSeedPath);
                }
            }
            StringBuilder res = new StringBuilder();
            int rules = compactIssues.keySet().size();
            int seqCount = 0;
            int allValidMutantNumber = 0;
            for(Map.Entry<String, HashMap<String, ArrayList<TriTuple>>> entry : compactIssues.entrySet()) {
                HashMap<String, ArrayList<TriTuple>> seq2mutants = entry.getValue();
                res.append("Rule: " + entry.getKey() + " Seq Size: " + seq2mutants.size() + "\n");
                seqCount += seq2mutants.size();
                for(Map.Entry<String, ArrayList<TriTuple>> subEntry : seq2mutants.entrySet()) {
                    res.append("Transoformation Sequence: " + subEntry.getKey() + "\n");
                    for(TriTuple triTuple : subEntry.getValue()) {
                        res.append(triTuple + "\n");
                    }
                    allValidMutantNumber += subEntry.getValue().size();
                }
            }
            res.append("Rule Size: " + rules + "\n");
            res.append("Unique Sequence: " + seqCount + "\n");
            res.append("Valid Mutant Size: " + allValidMutantNumber + "\n");
            res.append("Invalid Seed Size: " + invalidSeed + "\n");
            res.append("Valid Seed Size: " + validSeed + "\n");
//            System.out.println("Passed-check Transform: " + sumMutation);
            res.append("Succ Transform: " + succMutation + "\n");
            res.append("Fail Transform: " + failMutation + "\n");
            long executionTime = System.currentTimeMillis() - startTimeStamp;
            res.append(String.format(
                    "Overall Execution Time is: " + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(executionTime),
                            TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))) + "\n"));
            File resFile = new File(userdir + sep + "Multithread_Main_PMDSeeds1_Iter2_res.log");
            if(!resFile.exists()) {
                resFile.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(resFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(res.toString());
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}