package edu.polyu.report;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.EVALUATION_PATH;
import static edu.polyu.util.Utility.MUTANT_FOLDER;
import static edu.polyu.util.Utility.PROJECT_PATH;
import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;
import static edu.polyu.util.Utility.sep;

public class SonarQubeReport extends Report {

    public SonarQubeReport(String filePath) {
        super(filePath);
    }

    public void addViolation(Violation violation) {
        this.violations.add(violation);
    }

    public void addViolations(List<Violation> violations) {
        this.violations.addAll(violations);
    }

    public List<Violation> getViolations() {
        return this.violations;
    }

    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public String toString() {
        return "SQ Report: " + this.filePath + " Size: " + this.violations.size();
    }

    public static Report readSingleResultFile(String filePath, String jsonContent) {
        SonarQubeReport report = new SonarQubeReport(filePath);
        JSONObject root = new JSONObject(jsonContent);
        int total = root.getInt("total");
        if(total > 10000) {
            System.out.println("Exceed the warning limitation!");
            return report;
        }
        if (file2report.containsKey(filePath)) {
            System.out.println("Repeat process: " + filePath);
            return report;
        }
        file2report.put(filePath, report);
        file2row.put(filePath, new ArrayList<>());
        file2bugs.put(filePath, new HashMap<>());
        if(total == 0) {
            return report;
        }
        JSONArray issues = root.getJSONArray("issues");
        for(int i = 0; i < issues.length(); i++) {
            try {
                JSONObject issue = (JSONObject) issues.get(i);
                if(issue.has("component") && issue.has("textRange")) {
                    String ruleName = issue.getString("rule");
                    String component = issue.getString("component");
                    String relativePath = component.split(":")[1];
                    String readFilePath;
                    // judge seed or mutant
                    if(relativePath.startsWith("seeds_checker1" + sep) || relativePath.startsWith("seeds" + sep)) {
                        readFilePath = PROJECT_PATH + sep + relativePath;
                    } else {
                        readFilePath = MUTANT_FOLDER.getAbsolutePath() + sep + relativePath;
                    }
                    if(!filePath.equals(readFilePath)) {
                        System.out.println("Read Path: " + readFilePath);
                        System.out.println("Error in: " + filePath);
                        continue;
                    }
                    JSONObject textRange = (JSONObject) issue.get("textRange");
                    int startLine = textRange.getInt("startLine");
                    int endLine = textRange.getInt("endLine");
                    int startOffset = textRange.getInt("startOffset");
                    int endOffset = textRange.getInt("endOffset");
                    SonarQubeViolation violation = new SonarQubeViolation(ruleName, startLine, endLine, startOffset, endOffset);
                    report.addViolation(violation);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (Violation violation : report.getViolations()) {
            file2row.get(filePath).add(violation.getBeginLine());
            HashMap<String, List<Integer>> bug2cnt = file2bugs.get(filePath);
            if (!bug2cnt.containsKey(violation.getBugType())) {
                bug2cnt.put(violation.getBugType(), new ArrayList<>());
            }
            bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
        }
        return report;
    }

    public static void readSonarQubeResultFile(String filePath, String jsonContent) {
        Map<String, Report> path2report = new HashMap<>();
        JSONObject root = new JSONObject(jsonContent);
        int total = root.getInt("total");
        if(total > 10000) {
            System.err.println("Error in rule: " + filePath);
            return;
        }
        if(total == 0) {
            return;
        }
        JSONArray issues = root.getJSONArray("issues");
        for(int i = 0; i < issues.length(); i++) {
            try {
                JSONObject issue = (JSONObject) issues.get(i);
                if(issue.has("rule") && issue.has("textRange")) {
                    String ruleType = issue.getString("rule");
                    JSONObject textRange = (JSONObject) issue.get("textRange");
                    int startLine = textRange.getInt("startLine");
                    int endLine = textRange.getInt("endLine");
                    int startOffset = textRange.getInt("startOffset");
                    int endOffset = textRange.getInt("endOffset");
                    if (!path2report.containsKey(filePath)) {
                        Report newReport = new SonarQubeReport(filePath);
                        path2report.put(filePath, newReport);
                    }
                    Report report = path2report.get(filePath);
                    SonarQubeViolation violation = new SonarQubeViolation(ruleType, startLine, endLine, startOffset, endOffset);
                    report.addViolation(violation);
                }
            } catch (JSONException e) {
                System.err.println(jsonContent);
                e.printStackTrace();
            }
        }
        for (Report report : path2report.values()) {
            if(file2report.containsKey(report.getFilePath())) {
                SonarQubeReport r = (SonarQubeReport) file2report.get(report.getFilePath());
                r.addViolations(report.getViolations());
            } else {
                file2report.put(report.getFilePath(), report);
            }
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

    // Deprecated: This is a CNES version.
    @Deprecated
    public static void readSonarQubeResultFile(String reportPath) {
        if (DEBUG) {
            System.out.println("SonarQube Detection Result FileName: " + reportPath);
        }
        HashMap<String, SonarQubeReport> name2report = new HashMap<>();
//        final String[] FILE_HEADER = {"severity", "updateDate", "comments",	"line", "author", "rule", "project", "effort", "message",
//                "creationDate", "type",	"tags", "component", "flows", "scope", "textRange",	"debt", "key",	"hash", "status"};
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            System.out.println("Report Path: " + reportFile.getAbsoluteFile());
            System.exit(-1);
        }
        Reader reader;
        CSVParser format;
        List<CSVRecord> records = null;
        try {
            reader = new FileReader(reportPath);
            format = CSVFormat.EXCEL.withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .withDelimiter('\t')
                    .parse(reader);
            records = format.getRecords();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find SonarQube report file by path!");
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("IO error in parsing SonarQube report!");
            System.exit(-1);
        }
        String lineNumber, bugType, component, flows;
        if (records.isEmpty()) {
            System.err.println("Empty Record Path: " + reportPath);
            System.exit(-1);
        }
        CSVRecord testRecord = records.get(0);
        boolean textRange = false;
        for (String item : testRecord.toList()) {
            if (item.equals("textRange")) {
                textRange = true;
            }
        }
        for (CSVRecord record : records) {
            if (textRange) {
                String tuples = record.get("line");
                int startIndex = tuples.indexOf('=');
                int endIndex = tuples.indexOf(',');
                lineNumber = tuples.substring(startIndex + 1, endIndex);
            } else {
                lineNumber = record.get("line");
            }
            if (lineNumber.trim().equals("-")) {
                continue;
            }
            bugType = record.get("rule");
            component = record.get("component");
            flows = record.get("flows");
            String file;
            if (component.contains(".java")) {
                file = component;
            } else {
                if (flows.contains(".java")) {
                    file = flows;
                } else {
                    continue;
                }
            }
            String filepath;
            if (reportPath.contains("iter0")) {
                filepath = PROJECT_PATH + sep + file.substring(file.indexOf(":") + 1);
            } else {
                filepath = EVALUATION_PATH + sep + file.substring(file.indexOf(":") + 1);
            }
            if (!name2report.containsKey(filepath)) {
                SonarQubeReport report = new SonarQubeReport(filepath);
                name2report.put(filepath, report);
            }
            SonarQubeReport report = name2report.get(filepath);
            if (lineNumber.contains(".0")) {
                lineNumber = lineNumber.substring(0, lineNumber.length() - 2);
            }
//            SonarQubeViolation violation = new SonarQubeViolation(bugType, Integer.parseInt(lineNumber));
//            report.addViolation(violation);
        }

        for (SonarQubeReport report : name2report.values()) {
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