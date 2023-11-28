package edu.polyu.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static edu.polyu.util.Utility.failedReportPaths;
import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;

public class InferReport extends Report {

    public InferReport(String filePath) {
        super(filePath);
    }

    // seedFolderPath has iter depth information
    public static void readSingleInferResultFile(String seedPath, String reportPath) {
        Report report = new InferReport(seedPath);
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
                JsonNode node = rootNode.get(i);
                int row = node.get("line").asInt();
                String bugType = node.get("bug_type").asText();
                Violation violation = new InferViolation(row, bugType);
                report.addViolation(violation);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Violation violation : report.getViolations()) {
            file2row.get(seedPath).add(violation.getBeginLine());
            HashMap<String, List<Integer>> bug2cnt = file2bugs.get(seedPath);
            if (!bug2cnt.containsKey(violation.getBugType())) {
                bug2cnt.put(violation.getBugType(), new ArrayList<>());
            }
            bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
        }
    }

}
