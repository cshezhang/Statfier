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
import static edu.polyu.Util.AST_TESTING;
import static edu.polyu.Util.AST_TESTING_PATH;
import static edu.polyu.Util.CHECKSTYLE_MUTATION;
import static edu.polyu.Util.GUIDED_RANDOM_TESTING;
import static edu.polyu.Util.INFER_MUTATION;
import static edu.polyu.Util.MAIN_EXECUTION;
import static edu.polyu.Util.PMD_MUTATION;
import static edu.polyu.Util.PURE_RANDOM_TESTING;
import static edu.polyu.Util.PURE_TESTING;
import static edu.polyu.Util.SINGLE_TESTING_PATH;
import static edu.polyu.Util.SPOTBUGS_MUTATION;
import static edu.polyu.Util.compactIssues;
import static edu.polyu.Util.initEnv;
import static edu.polyu.Util.sep;
import static edu.polyu.Util.sourceSeedPath;
import static edu.polyu.Util.startTimeStamp;
import static edu.polyu.Util.userdir;

/**
 * Description: Main Process for automatic testing
 * Author: Vanguard
 * Date: 2021-08-08 16:20
 */
public class AutomaticTester {

    public static void main(String[] args) {
        initEnv();
        Schedule schedule = Schedule.getInstance();
        if(AST_TESTING) {
            schedule.testAST(AST_TESTING_PATH);
            System.exit(0);
        }
        if(PURE_TESTING) {
            schedule.pureTesting(SINGLE_TESTING_PATH);
            System.exit(0);
        }
        try {
            if (PURE_RANDOM_TESTING) {
                schedule.pureRandomTesting(sourceSeedPath);
            }
            if (GUIDED_RANDOM_TESTING) {
//                tester.guidedRandomTesting(sourceSeedPath);
                if(PMD_MUTATION) {
                    schedule.executePMDMutation(sourceSeedPath);
                }
                if(SPOTBUGS_MUTATION) {
                    schedule.executeSpotBugsMutation(sourceSeedPath);
                }
            }
            if (MAIN_EXECUTION) {
                if(PMD_MUTATION) {
                    schedule.executePMDMutation(sourceSeedPath);
                }
                if(SPOTBUGS_MUTATION) {
                    schedule.executeSpotBugsMutation(sourceSeedPath);
                }
                if(CHECKSTYLE_MUTATION) {
                    schedule.executeCheckStyleMutation(sourceSeedPath);
                }
                if(INFER_MUTATION) {
                    schedule.executeInferMutation(sourceSeedPath);
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