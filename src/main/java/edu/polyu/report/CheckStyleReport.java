package edu.polyu.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;

public class CheckStyleReport extends Report {

    public CheckStyleReport(String filePath) {
        super(filePath);
    }

    public static void readCheckStyleResultFile(String reportPath) { // Actually, this is readSingleCheckStyleResultFile
        HashMap<String, Report> name2report = new HashMap<>();
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            return;
        }
        List<String> errorInstances = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(reportPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("[ERROR]")) {
                    errorInstances.add(line);
                }
            }
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filepath;
        for (String errorInstance : errorInstances) {
            int startIndex = errorInstance.indexOf(' ') + 1, endIndex = -1;
            for (int i = startIndex + 1; i < errorInstance.length(); i++) {
                if (errorInstance.charAt(i) == ' ') {
                    endIndex = i;
                    break;
                }
            }
            if (endIndex == -1) {
                return;
            }
            String content = errorInstance.substring(startIndex, endIndex);
            int index1 = content.indexOf(".java") + ".java".length(), index2 = -1;
            if (content.charAt(index1) != ':') {
                return;
            }
            for (int i = index1 + 1; i < content.length(); i++) {
                if (content.charAt(i) == ':') {
                    index2 = i;
                    break;
                }
            }
            filepath = content.substring(0, index1);
            int row = 0;
            try {
                row = Integer.parseInt(content.substring(index1 + 1, index2));
            } catch (Exception e) {
                e.printStackTrace();
            }
            index1 = errorInstance.lastIndexOf('[');
            String bugType = errorInstance.substring(index1 + 1, errorInstance.length() - 1);
            Violation violation = new CheckStyleViolation(filepath, row, bugType);
            if (name2report.containsKey(filepath)) {
                name2report.get(filepath).addViolation(violation);
            } else {
                Report newReport = new CheckStyleReport(filepath);
                newReport.addViolation(violation);
                name2report.put(filepath, newReport);
            }
        }
        for (Report report : name2report.values()) {
            file2report.put(report.getFilePath(), report);
            if (!file2row.containsKey(report.getFilePath())) {
                file2row.put(report.getFilePath(), new ArrayList<>());
                file2bugs.put(report.getFilePath(), new HashMap<>());
            }
            for (Violation violation : report.getViolations()) {
                file2row.get(report.getFilePath()).add(violation.getBeginLine());
                HashMap<String, List<Integer>> bug2cnt = file2bugs.get(report.getFilePath());
                if (!bug2cnt.containsKey(violation.getBugType())) {
                    bug2cnt.put(violation.getBugType(), new ArrayList<>());
                }
                bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
            }
        }
    }

}
