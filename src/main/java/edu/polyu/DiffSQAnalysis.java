package edu.polyu;

import edu.polyu.report.SonarQube_Report;
import edu.polyu.report.SonarQube_Violation;
import edu.polyu.util.Util;
import org.junit.Test;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.SonarClient;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.issue.Issue;
import org.sonar.wsclient.issue.IssueClient;
import org.sonar.wsclient.issue.IssueQuery;
import org.sonar.wsclient.issue.Issues;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static edu.polyu.util.Util.getDirectFilenamesFromFolder;
import static edu.polyu.util.Util.readFileByLine;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/4/21 17:12
 */
public class DiffSQAnalysis {

    private static Map<String, Map<String, Integer>> file2bug = new HashMap<>();
    private static Map<String, SonarQube_Report> file2report = new HashMap<>();
    private static Map<String, String> seed2mutant = new HashMap<>();
    private static Map<String, String> file2trans = new HashMap<>();

    public static void analysis(String mappingPath, String reportPath1, String reportPath2) {
        System.out.println("Report Path1: " + reportPath1);
        System.out.println("Report Path2: " + reportPath2);
        List<SonarQube_Report> reports1 = Util.readSonarQubeResultFile(reportPath1);
//        System.exit(-1);
        List<SonarQube_Report> reports2 = Util.readSonarQubeResultFile(reportPath2);
        for(SonarQube_Report report : reports1) {
            file2report.put(report.getFilepath(), report);
            Set<String> bugs = new HashSet<>();
            for(SonarQube_Violation violation : report.getViolations()) {
                bugs.add(violation.getBugType());
            }
            file2bug.put(report.getFilepath(), bugs);
        }
        for(SonarQube_Report report : reports2) {
            file2report.put(report.getFilepath(), report);
            Set<String> bugs = new HashSet<>();
            for(SonarQube_Violation violation : report.getViolations()) {
                bugs.add(violation.getBugType());
            }
            file2bug.put(report.getFilepath(), bugs);
        }
        List<String> lines = readFileByLine(mappingPath);
        for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if(line.contains("->")) {
                int index1 = line.indexOf("->");
                int index2 = line.indexOf("#");
                String seedPath = line.substring(0, index1);
                String mutantPath = line.substring(index1 + 2, index2);
                String transType = line.substring(index2 + 1);
                seed2mutant.put(seedPath, mutantPath);
                file2trans.put(line, transType);
            }
        }
        List<String> outputLines = new ArrayList<>();
        int bugCount = 0;
        for(Map.Entry<String, String> entry : seed2mutant.entrySet()) {
            Set<String> seedBugs = file2bug.get(entry.getKey());
            Set<String> mutantBugs = file2bug.get(entry.getValue());
            List<String> FNs = new ArrayList<>();
            List<String> FPs = new ArrayList<>();
            for(String bug : seedBugs) {
                if(!mutantBugs.contains(bug)) {
                }

            }
        }
        System.out.println("Sum All Bugs: " + bugCount);
    }

    @Test
    public void getSonarQubeIssues() {
        SonarClient client = SonarClient.create("http://localhost:9000");
        client.builder().login("admin");
        client.builder().password("z123456");
        IssueQuery query = IssueQuery.create();
        query.components("GuidedDiv_Seed1");
//        query.severities("CRITICAL", "MAJOR", "MINOR");
        query.severities("MINOR");
        IssueClient issueClient = client.issueClient();
        Issues issues = issueClient.find(query);
        List<Issue> issueList = issues.list();
        for(Issue issue : issueList) {
            System.out.println(issue);
        }
    }


    public static void main(String[] args) {
        String seed_report = "/home/vanguard/evaluation/" + File.separator + "SonarQube_Seeds1.csv";
        String report2 = "/home/vanguard/evaluation/" + File.separator + "SonarQube_Testing.csv";
        String mappingPath = "/home/vanguard/evaluation/SonarQube_Testing/Output.log";
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
