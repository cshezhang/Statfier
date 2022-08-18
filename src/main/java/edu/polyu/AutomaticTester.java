package edu.polyu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.polyu.util.Schedule;
import edu.polyu.util.TriTuple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static edu.polyu.analysis.TypeWrapper.failMutation;
import static edu.polyu.analysis.TypeWrapper.invalidSeed;
import static edu.polyu.analysis.TypeWrapper.mutant2seq;
import static edu.polyu.analysis.TypeWrapper.mutant2seed;
import static edu.polyu.analysis.TypeWrapper.succMutation;
import static edu.polyu.analysis.TypeWrapper.validSeed;
import static edu.polyu.transform.Transform.cnt1;
import static edu.polyu.transform.Transform.cnt2;
import static edu.polyu.util.Invoker.failedCommands;
import static edu.polyu.util.Utility.CHECKSTYLE_MUTATION;
import static edu.polyu.util.Utility.INFER_MUTATION;
import static edu.polyu.util.Utility.PMD_MUTATION;
import static edu.polyu.util.Utility.SONARQUBE_MUTATION;
import static edu.polyu.util.Utility.SPOTBUGS_MUTATION;
import static edu.polyu.util.Utility.compactIssues;
import static edu.polyu.util.Utility.failedReport;
import static edu.polyu.util.Utility.initEnv;
import static edu.polyu.util.Utility.sourceSeedPath;
import static edu.polyu.util.Utility.startTimeStamp;
import static edu.polyu.util.Utility.EVALUATION_PATH;

/**
 * Description: Main Process for automatic testing
 * Author: Vanguard
 * Date: 2021-08-08 16:20
 */
public class AutomaticTester {

    public static void main(String[] args) {
        long time1 = System.currentTimeMillis();
        initEnv();
        Schedule schedule = Schedule.getInstance();
        try {
            // Main Automatic Entry
            if (PMD_MUTATION) {
                schedule.executePMDMutation(sourceSeedPath);
            }
            if (SPOTBUGS_MUTATION) {
                schedule.executeSpotBugsMutation(sourceSeedPath);
            }
            if (CHECKSTYLE_MUTATION) {
                schedule.executeCheckStyleMutation(sourceSeedPath);
            }
            if (INFER_MUTATION) {
                schedule.executeInferMutation(sourceSeedPath);
            }
            if (SONARQUBE_MUTATION) {
                schedule.executeSonarQubeMutation(sourceSeedPath);
            }
            int rules = compactIssues.keySet().size();
            int seqCount = 0;
            int allValidMutantNumber = 0;
            for (Map.Entry<String, HashMap<String, List<TriTuple>>> entry : compactIssues.entrySet()) {
                String rule = entry.getKey();
                HashMap<String, List<TriTuple>> seq2mutants = entry.getValue();
                seqCount += seq2mutants.size();
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode root = mapper.createObjectNode();
                root.put("Rule", rule);
                root.put("SeqSize", seq2mutants.size());
                ArrayNode bugs = mapper.createArrayNode();
                for (Map.Entry<String, List<TriTuple>> subEntry : seq2mutants.entrySet()) {
                    ObjectNode bug = mapper.createObjectNode();
                    bug.put("Transform_Sequence", subEntry.getKey());
                    ArrayNode tuples = mapper.createArrayNode();
                    for (TriTuple triTuple : subEntry.getValue()) {
                        ObjectNode tuple = mapper.createObjectNode();
                        tuple.put("Seed", triTuple.first);
                        tuple.put("Mutant", triTuple.second);
                        tuple.put("BugType", triTuple.third);
                        tuples.add(tuple);
                    }
                    bug.put("Bugs", tuples);
                    bugs.add(bug);
                    allValidMutantNumber += subEntry.getValue().size();
                }
                root.put("Results", bugs);
                File jsonFile = new File(EVALUATION_PATH + File.separator + "results" + File.separator + rule + ".json");
                if(!jsonFile.exists()) {
                    jsonFile.createNewFile();
                }
                FileWriter jsonWriter = new FileWriter(jsonFile);
                BufferedWriter jsonBufferedWriter = new BufferedWriter(jsonWriter);
                jsonBufferedWriter.write(root.toString());
                jsonBufferedWriter.close();
                jsonWriter.close();
            }
            StringBuilder res = new StringBuilder();
            res.append("Rule Size: " + rules + "\n");
            res.append("Detected Rules: " + compactIssues.keySet() + "\n");
            res.append("Unique Sequence: " + seqCount + "\n");
            res.append("Valid Mutant Size(Potential Bug): " + allValidMutantNumber + "\n");
            res.append("Invalid Seed Size: " + invalidSeed + "\n");
            res.append("Valid Seed Size: " + validSeed + "\n");
            res.append("Succ Transform: " + succMutation + "\n");
            res.append("Fail Transform: " + failMutation + "\n");
            res.append("Mutant2Seed:\n");
            for(Map.Entry<String, String> entry : mutant2seed.entrySet()) {
                res.append(entry.getKey() + "->" + entry.getValue() + "#" + mutant2seq.get(entry.getKey()) + "\n");
            }
            long executionTime = System.currentTimeMillis() - startTimeStamp;
            res.append(String.format(
                    "Overall Execution Time is: " + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(executionTime),
                            TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))) + "\n")
            );
            File resFile = new File(EVALUATION_PATH + File.separator + "Output.log");
            if (!resFile.exists()) {
                resFile.createNewFile();
            }
            FileWriter writer = new FileWriter(resFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(res.toString());
            if(INFER_MUTATION) {
                bufferedWriter.write("Failed Reports:\n");
                for(String report : failedReport) {
                    bufferedWriter.write(report + "\n");
                }
            }
            if(SPOTBUGS_MUTATION) {
                bufferedWriter.write("Failed Commands:\n");
                for(String failedCmd : failedCommands) {
                    bufferedWriter.write(failedCmd + "\n");
                }
            }
            bufferedWriter.close();
            writer.close();
            System.out.println("Cnt1: " + cnt1);
            System.out.println("Cnt2: " + cnt2);
            System.out.println("Ratio: " + cnt2 / (double)(cnt1));
            long OVERALL_EXEC_TIME = System.currentTimeMillis() - time1;
            long minutes = (OVERALL_EXEC_TIME / 1000) / 60;
            long seconds = (OVERALL_EXEC_TIME / 1000) % 60;
            System.out.format("%d min(s) %d sec(s).", minutes, seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

