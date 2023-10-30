package edu.polyu.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;

/**
 * Description: PMD report class to save different info in a PMD report
 * Author: RainyD4y
 * Date: 2021/9/23 7:30
 */
public class PMD_Report implements Report {

    private String filePath;
    private List<PMD_Violation> violations;

    public PMD_Report(String filePath) {
        this.filePath = filePath;
        this.violations = new ArrayList<>();
    }

    public void addViolation(PMD_Violation pmd_violation) {
        this.violations.add(pmd_violation);
    }

    public String getFilePath() {
        return this.filePath;
    }

    public List<PMD_Violation> getViolations() {
        return this.violations;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("PMD_Report Path: " + this.filePath + "\n");
        for(PMD_Violation pmd_violation : this.violations) {
            out.append(pmd_violation.toString() + "\n");
        }
        return out.toString();
    }

    public static List<String> errorReportPaths = new ArrayList<>();

    // Read PMD result file which includes only one seed file.
    public static void readSinglePMDResultFile(final String reportPath, String detectionPath) {
        ObjectMapper mapper = new ObjectMapper();
        File reportFile = new File(reportPath);
        if(!reportFile.exists()) {
            System.err.println("Report not existed: " + reportPath);
            return;
        }
        if(file2report.containsKey(detectionPath)) {
            System.err.println("Repeat Process ReportPath: " + reportPath);
            return;
        }
        PMD_Report report = new PMD_Report(detectionPath);
        file2report.put(detectionPath, report);
        file2row.put(detectionPath, new ArrayList<>());
        file2bugs.put(detectionPath, new HashMap<>());
        try {
            JsonNode rootNode = mapper.readTree(reportFile);
            JsonNode reportNodes = rootNode.get("files");
            for (int i = 0; i < reportNodes.size(); i++) {
                JsonNode reportNode = reportNodes.get(i);
                String filePath = reportNode.get("filename").asText();
                if(!filePath.equals(detectionPath)) {
                    System.err.println("Error ReportPath: " + reportPath);
                    System.err.println("Error SeedPath: " + detectionPath);
                    System.exit(-1);
                }
                JsonNode violationNodes = reportNode.get("violations");
                for (int j = 0; j < violationNodes.size(); j++) {
                    JsonNode violationNode = violationNodes.get(j);
                    PMD_Violation violation = new PMD_Violation(violationNode);
                    report.addViolation(violation);
                }
            }
            JsonNode processErrorNodes = rootNode.get("processingErrors");
            JsonNode configErrorNodes = rootNode.get("configurationErrors");
            if(processErrorNodes.size() > 0 || configErrorNodes.size() > 0) {
                errorReportPaths.add(reportPath);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Exceptional Json Path:" + reportPath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (PMD_Violation violation : report.getViolations()) {
            file2row.get(detectionPath).add(violation.beginLine);
            HashMap<String, List<Integer>> bug2cnt = file2bugs.get(detectionPath);
            if (!bug2cnt.containsKey(violation.getBugType())) {
                bug2cnt.put(violation.getBugType(), new ArrayList<>());
            }
            bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
        }
    }

    public static void readPMDResultFile(final String reportPath) {
        List<PMD_Report> pmd_reports = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(reportPath);
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);
            JsonNode reportNodes = rootNode.get("files");
            for (int i = 0; i < reportNodes.size(); i++) {
                JsonNode reportNode = reportNodes.get(i);
                PMD_Report newReport = new PMD_Report(reportNode.get("filename").asText());
                JsonNode violationNodes = reportNode.get("violations");
                for (int j = 0; j < violationNodes.size(); j++) {
                    JsonNode violationNode = violationNodes.get(j);
                    PMD_Violation violation = new PMD_Violation(violationNode);
                    newReport.addViolation(violation);
                }
                pmd_reports.add(newReport);
            }
            JsonNode processErrorNodes = rootNode.get("processingErrors");
            JsonNode configErrorNodes = rootNode.get("configurationErrors");
            if(processErrorNodes.size() > 0 || configErrorNodes.size() > 0) {
                errorReportPaths.add(reportPath);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Exceptional Json Path:" + reportPath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (PMD_Report report : pmd_reports) {
            file2report.put(report.getFilePath(), report);
            if (!file2row.containsKey(report.getFilePath())) {
                file2row.put(report.getFilePath(), new ArrayList<>());
                file2bugs.put(report.getFilePath(), new HashMap<>());
            }
            for (PMD_Violation violation : report.getViolations()) {
                file2row.get(report.getFilePath()).add(violation.beginLine);
                HashMap<String, List<Integer>> bug2cnt = file2bugs.get(report.getFilePath());
                if (!bug2cnt.containsKey(violation.getBugType())) {
                    bug2cnt.put(violation.getBugType(), new ArrayList<>());
                }
                bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
            }
        }
    }

}
