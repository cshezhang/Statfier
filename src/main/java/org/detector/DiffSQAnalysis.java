package org.detector;

import org.detector.util.TriTuple;
import org.detector.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.detector.util.Utility.readFileByLine;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2022/8/29 15:18
 */
public class DiffSQAnalysis {

    private static Map<String, Map<String, Set<Integer>>> file2bug = new HashMap<>();
    private static Map<String, String> seed2mutant = new HashMap<>();
    private static Map<String, String> file2trans = new HashMap<>();

    public static void analysis(String mappingPath, String reportPath1, String reportPath2) {
        System.out.println("Report Path1: " + reportPath1);
        System.out.println("Report Path2: " + reportPath2);
        Utility.readSonarQubeResultFile(reportPath1);
        Utility.readSonarQubeResultFile(reportPath2);
        List<String> lines = readFileByLine(mappingPath);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("->")) {
                int index1 = line.indexOf("->");
                int index2 = line.indexOf("#");
                String seedPath = line.substring(0, index1);
                String mutantPath = line.substring(index1 + 2, index2);
                String transType = line.substring(index2 + 1);
                seed2mutant.put(seedPath, mutantPath);
                file2trans.put(seedPath + mutantPath, transType);
            }
        }
        HashMap<String, HashMap<String, ArrayList<TriTuple>>> compactIssues = new HashMap<>();
        for (Map.Entry<String, String> entry : seed2mutant.entrySet()) {
            String seedPath = entry.getKey(), mutantPath = entry.getValue();
            String transSeq = file2trans.get(seedPath + mutantPath);
            Map<String, Set<Integer>> bug2cnt1 = file2bug.get(seedPath); // seed
            Map<String, Set<Integer>> bug2cnt2 = file2bug.get(mutantPath); // mutant
            if(bug2cnt2 != null) {
                System.out.println(mutantPath);
                for (Map.Entry<String, Set<Integer>> b2c : bug2cnt1.entrySet()) {
                    String rule = b2c.getKey();
                    if ((!bug2cnt2.containsKey(rule)) ||
                            (bug2cnt2.containsKey(rule) && bug2cnt2.get(rule).size() < bug2cnt1.get(rule).size())) { // seed has, mutant not
                        if (!compactIssues.containsKey(rule)) {
                            HashMap<String, ArrayList<TriTuple>> trans2tuples = new HashMap<>();
                            compactIssues.put(rule, trans2tuples);
                        }
                        HashMap<String, ArrayList<TriTuple>> trans2tuples = compactIssues.get(rule);
                        if (!trans2tuples.containsKey(transSeq)) {
                            trans2tuples.put(transSeq, new ArrayList<>());
                        }
                        trans2tuples.get(transSeq).add(new TriTuple(seedPath, mutantPath, "FN"));
                    }
                }
            } else {
//                System.out.println(mutantPath);
                continue;
            }
            for (Map.Entry<String, Set<Integer>> b2c : bug2cnt2.entrySet()) {
                String rule = b2c.getKey();
                boolean isFP = false;
                if ((bug2cnt1.containsKey(rule))) { // seed has, mutant not
                    if (transSeq.contains("AddControlBranch")) {
                        if (bug2cnt1.get(rule).size() + 1 < bug2cnt2.get(rule).size()) {
                            isFP = true;
                        }
                    } else {
                        if (bug2cnt1.get(rule).size() < bug2cnt2.get(rule).size()) {
                            isFP = true;
                        }
                    }
                } else {
                    isFP = true;
                }
                if (isFP) {
                    if (!compactIssues.containsKey(rule)) {
                        HashMap<String, ArrayList<TriTuple>> trans2tuples = new HashMap<>();
                        compactIssues.put(rule, trans2tuples);
                    }
                    HashMap<String, ArrayList<TriTuple>> trans2tuples = compactIssues.get(rule);
                    if (!trans2tuples.containsKey(transSeq)) {
                        trans2tuples.put(transSeq, new ArrayList<>());
                    }
                    trans2tuples.get(transSeq).add(new TriTuple(seedPath, mutantPath, "FP"));
                }
            }
        }
        System.out.println("Sum All Bugs: " + compactIssues.size());
        for(String key : compactIssues.keySet()) {
            HashMap<String, ArrayList<TriTuple>> trans2bugs = compactIssues.get(key);
            for(String transform : trans2bugs.keySet()) {
                System.out.println("Rule: " + key + " " + transform);
                ArrayList<TriTuple> bugs = trans2bugs.get(transform);
                for(TriTuple tuple : bugs) {
                    System.out.println(tuple);
                }
            }
        }
    }

    public static void main(String[] args) {
        String seed_report = "/home/vanguard/evaluation/2022-04-25-SonarQube_Seeds1-issues-report.csv";
        String report2 = "/home/vanguard/evaluation/2022-04-25-SonarQube_Testing-issues-report.csv";
        String mappingPath = "/home/vanguard/evaluation/SonarQube_Test2/Output.log";
        analysis(mappingPath, seed_report, report2);
        // String SONARQUBE_EVALUATION_PATH = "/home/vanguard/evaluation/SonarQube_Evaluation";
        // List<String> folderPaths = getDirectFilenamesFromFolder(SONARQUBE_EVALUATION_PATH, true);
        // for(String folderPath : folderPaths) {
        //     String report2 = folderPath + File.separator + "report2.csv";
        //     String mappingPath = folderPath + File.separator + "Outoput.log";
        //     analysis(mappingPath, seed_report, report2);
        // }
    }
}
