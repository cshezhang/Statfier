package org.detector.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.detector.util.Utility.failedReportPaths;
import static org.detector.util.Utility.file2bugs;
import static org.detector.util.Utility.file2report;
import static org.detector.util.Utility.file2row;

public class Infer_Report implements Report {

    private String filePath;
    private List<Infer_Violation> violations;

    public Infer_Report(String filePath) {
        this.filePath = filePath;
        this.violations = new ArrayList<>();
    }

    public List<Infer_Violation> getViolations() {
        return this.violations;
    }

    public void addViolation(Infer_Violation violation) {
        this.violations.add(violation);
    }

    public String getFilePath() {
        return this.filePath;
    }

    // seedFolderPath has iter depth information
    public static void readSingleInferResultFile(String seedPath, String reportPath) {
        Infer_Report report = new Infer_Report(seedPath);
        ObjectMapper mapper = new ObjectMapper();
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            failedReportPaths.add(reportPath);
            return;
        }
        if(file2report.containsKey(seedPath)) {
            System.out.println("Repeat process: " + seedPath);
            System.out.println("Report path: " + reportPath);
            System.exit(-1);
        }
        file2report.put(seedPath, report);
        file2row.put(seedPath, new ArrayList<>());
        file2bugs.put(seedPath, new HashMap<>());
        try {
            JsonNode rootNode = mapper.readTree(reportFile);
            for (int i = 0; i < rootNode.size(); i++) {
                JsonNode violationNode = rootNode.get(i);
                Infer_Violation infer_violation = new Infer_Violation(violationNode);
                report.addViolation(infer_violation);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Infer_Violation violation : report.getViolations()) {
            file2row.get(seedPath).add(violation.getBeginLine());
            HashMap<String, List<Integer>> bug2cnt = file2bugs.get(seedPath);
            if (!bug2cnt.containsKey(violation.getBugType())) {
                bug2cnt.put(violation.getBugType(), new ArrayList<>());
            }
            bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
        }
    }


//    // seedFolderPath has iter depth information
//    public static void readInferResultFile(String seedPath, String reportPath) {
//        HashMap<String, Infer_Report> name2report = new HashMap<>();
//        ObjectMapper mapper = new ObjectMapper();
//        File reportFile = new File(reportPath);
//        if (!reportFile.exists()) {
//            failedReport.add(reportPath);
//            return;
//        }
//        try {
//            JsonNode rootNode = mapper.readTree(reportFile);
//            for (int i = 0; i < rootNode.size(); i++) {
//                JsonNode violationNode = rootNode.get(i);
//                Infer_Violation infer_violation = new Infer_Violation(violationNode);
//                if (name2report.containsKey(seedPath)) {
//                    name2report.get(seedPath).addViolation(infer_violation);
//                } else {
//                    Infer_Report infer_report = new Infer_Report(seedPath);
//                    infer_report.addViolation(infer_violation);
//                    name2report.put(seedPath, infer_report);
//                }
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (Infer_Report report : name2report.values()) {
//            file2report.put(report.getFilepath(), report);
//            if (!file2row.containsKey(report.getFilepath())) {
//                file2row.put(report.getFilepath(), new ArrayList<>());
//                file2bugs.put(report.getFilepath(), new HashMap<>());
//            }
//            for (Infer_Violation violation : report.getViolations()) {
//                file2row.get(report.getFilepath()).add(violation.getBeginLine());
//                HashMap<String, List<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
//                if (!bug2cnt.containsKey(violation.getBugType())) {
//                    bug2cnt.put(violation.getBugType(), new ArrayList<>());
//                }
//                bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
//            }
//        }
//    }

}
