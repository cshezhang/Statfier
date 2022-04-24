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

import static edu.polyu.analysis.ASTWrapper.failMutation;
import static edu.polyu.analysis.ASTWrapper.invalidSeed;
import static edu.polyu.analysis.ASTWrapper.mutant2seq;
import static edu.polyu.analysis.ASTWrapper.seed2mutant;
import static edu.polyu.analysis.ASTWrapper.succMutation;
import static edu.polyu.analysis.ASTWrapper.validSeed;
import static edu.polyu.util.Util.CHECKSTYLE_MUTATION;
import static edu.polyu.util.Util.INFER_MUTATION;
import static edu.polyu.util.Util.PMD_MUTATION;
import static edu.polyu.util.Util.SONARQUBE_MUTATION;
import static edu.polyu.util.Util.SPOTBUGS_MUTATION;
import static edu.polyu.util.Util.compactIssues;
import static edu.polyu.util.Util.failedReport;
import static edu.polyu.util.Util.initEnv;
import static edu.polyu.util.Util.sourceSeedPath;
import static edu.polyu.util.Util.startTimeStamp;
import static edu.polyu.util.Util.userdir;

/**
 * Description: Main Process for automatic testing
 * Author: Vanguard
 * Date: 2021-08-08 16:20
 */
public class AutomaticTester {

    public static void main(String[] args) {
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
                File jsonFile = new File(userdir + File.separator + "results" + File.separator + rule + ".json");
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
            res.append("Seed2Mutant:\n");
            for(Map.Entry<String, String> entry : seed2mutant.entrySet()) {
                res.append(entry.getKey() + "->" + entry.getValue() + "#" + mutant2seq.get(entry.getValue()) + "\n");
            }
            long executionTime = System.currentTimeMillis() - startTimeStamp;
            res.append(String.format(
                    "Overall Execution Time is: " + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(executionTime),
                            TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))) + "\n")
            );
            File resFile = new File(userdir + File.separator + "Output.log");
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
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

