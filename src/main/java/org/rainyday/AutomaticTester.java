package org.rainyday;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.rainyday.util.Schedule;
import org.rainyday.util.TriTuple;
import org.rainyday.analysis.TypeWrapper;
import org.rainyday.util.Invoker;
import org.rainyday.util.Utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.rainyday.transform.Transform.cnt1;
import static org.rainyday.transform.Transform.cnt2;
import static org.rainyday.util.Utility.sourceSeedPath;

/**
 * Description: Main Process for automatic testing
 * Author: Vanguard
 * Date: 2021-08-08 16:20
 */
public class AutomaticTester {

    public static void main(String[] args) {
        long time1 = System.currentTimeMillis();
        Utility.initEnv();
        Schedule schedule = Schedule.getInstance();
        try {
            if (Utility.PMD_MUTATION) {
                schedule.executePMDTransform(sourceSeedPath);
            }
            if (Utility.SPOTBUGS_MUTATION) {
                schedule.executeSpotBugsTransform(sourceSeedPath);
            }
            if (Utility.CHECKSTYLE_MUTATION) {
                schedule.executeCheckStyleTransform(sourceSeedPath);
            }
            if (Utility.INFER_MUTATION) {
                schedule.executeInferTransform(sourceSeedPath);
            }
            if (Utility.SONARQUBE_MUTATION) {
                schedule.executeSonarQubeTransform(sourceSeedPath);
            }
            int rules = Utility.compactIssues.keySet().size();
            int seqCount = 0;
            int allValidVariantNumber = 0;
            for (Map.Entry<String, HashMap<String, List<TriTuple>>> entry : Utility.compactIssues.entrySet()) {
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
                    allValidVariantNumber += subEntry.getValue().size();
                }
                root.put("Results", bugs);
                File jsonFile = new File(Utility.EVALUATION_PATH + File.separator + "results" + File.separator + rule + ".json");
                if (!jsonFile.exists()) {
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
            res.append("Detected Rules: " + Utility.compactIssues.keySet() + "\n");
            res.append("Unique Sequence: " + seqCount + "\n");
            res.append("Valid Mutant Size(Potential Bug): " + allValidVariantNumber + "\n");
//            res.append("Invalid Seed Size: " + TypeWrapper.invalidSeed + "\n");
//            res.append("Valid Seed Size: " + TypeWrapper.validSeed + "\n");
//            res.append("Successful Transform: " + TypeWrapper.succMutation + "\n");
//            res.append("Failed Transform: " + TypeWrapper.failMutation + "\n");
            res.append("Mutant2Seed:\n");
            for (Map.Entry<String, String> entry : TypeWrapper.mutant2seed.entrySet()) {
                res.append(entry.getKey() + "->" + entry.getValue() + "#" + TypeWrapper.mutant2seq.get(entry.getKey()) + "\n");
            }
            long executionTime = System.currentTimeMillis() - Utility.startTimeStamp;
            res.append(String.format(
                    "Overall Execution Time is: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(executionTime),
                        TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))) + "\n")
            );
            File resFile = new File(Utility.EVALUATION_PATH + File.separator + "Output.log");
            if (!resFile.exists()) {
                resFile.createNewFile();
            }
            FileWriter writer = new FileWriter(resFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(res.toString());
            if (Utility.INFER_MUTATION) {
                bufferedWriter.write("Failed Reports:\n");
                for (String report : Utility.failedReport) {
                    bufferedWriter.write(report + "\n");
                }
            }
            if (Utility.SPOTBUGS_MUTATION) {
                bufferedWriter.write("Failed Commands:\n");
                for (String failedCmd : Invoker.failedCommands) {
                    bufferedWriter.write(failedCmd + "\n");
                }
            }
            bufferedWriter.close();
            writer.close();
            System.out.println("All variants size: " + cnt1);
            System.out.println("Reduced variants size: " + cnt2);
            System.out.println("Ratio: " + cnt1.get() / (double) (cnt1.get()));
            long OVERALL_EXEC_TIME = System.currentTimeMillis() - time1;
            long minutes = (OVERALL_EXEC_TIME / 1000) / 60;
            long seconds = (OVERALL_EXEC_TIME / 1000) % 60;
            System.out.format("%d min(s) %d sec(s).", minutes, seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

