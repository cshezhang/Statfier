package edu.polyu;

import edu.polyu.report.SonarQube_Report;
import edu.polyu.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.polyu.util.Util.getDirectFilenamesFromFolder;
import static edu.polyu.util.Util.readFileByLine;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/4/21 17:12
 */
public class DiffSQAnalysis {

    private static Map<String, Integer> file2bug = new HashMap<>();
    private static Map<String, SonarQube_Report> file2report = new HashMap<>();
    private static Map<String, String> seed2mutant = new HashMap<>();

    public static void analysis(String mappingPath, String reportPath1, String reportPath2) {
        List<SonarQube_Report> reports1 = Util.readSonarQubeResultFile(reportPath1);
        List<SonarQube_Report> reports2 = Util.readSonarQubeResultFile(reportPath2);
        for(SonarQube_Report report : reports1) {
            file2report.put(report.getFilepath(), report);
            file2bug.put(report.getFilepath(), report.getViolations().size());
        }
        for(SonarQube_Report report2 : reports2) {
            file2report.put(report2.getFilepath(), report2);
            file2bug.put(report2.getFilepath(), report2.getViolations().size());
        }
        List<String> lines = readFileByLine(mappingPath);
        for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if(line.contains("->")) {
                int index = line.indexOf("->");
                String seedPath = line.substring(0, index);
                String mutantPath = line.substring(index + 2);
                seed2mutant.put(seedPath, mutantPath);
            }
        }
        List<String> outputLines = new ArrayList<>();
        int bugCount = 0;
        for(Map.Entry<String, String> entry : seed2mutant.entrySet()) {
            if(file2bug.get(entry.getKey()) != file2bug.get(entry.getValue())) {
                bugCount++;
                outputLines.add(entry.getKey() + "->" +entry.getValue());
            }
        }
        System.out.println("Sum All Bugs: " + bugCount);
    }

    public static void main(String[] args) {
        String SONARQUBE_EVALUATION_PATH = "/home/vanguard/evaluation/SonarQube_Evaluation";
        List<String> folderPaths = getDirectFilenamesFromFolder(SONARQUBE_EVALUATION_PATH, true);
        String report1 = "" + File.separator + "report1.csv";
        for(String folderPath : folderPaths) {
            String report2 = folderPath + File.separator + "report2.csv";
            String mappingPath = folderPath + File.separator + "Outoput.log";
            analysis(mappingPath, report1, report2);
        }
    }

}
