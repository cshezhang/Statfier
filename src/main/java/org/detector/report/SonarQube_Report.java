package org.detector.report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.detector.util.Utility.EVALUATION_PATH;
import static org.detector.util.Utility.sep;

public class SonarQube_Report implements Report {

    private String filepath;
    private List<SonarQube_Violation> violations;

    public SonarQube_Report(String filepath) {
        this.filepath = filepath;
        this.violations = new ArrayList<>();
    }

    public void addViolation(SonarQube_Violation violation) {
        this.violations.add(violation);
    }

    public List<SonarQube_Violation> getViolations() {
        return this.violations;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public static void readSonarQubeResultFile(String ruleName, String jsonContent, Map<String, SonarQube_Report> path2report) {
        JSONObject root = new JSONObject(jsonContent);
        int total = root.getInt("total");
        if(total > 10000) {
            System.err.println("Error in rule: " + ruleName);
            return;
        }
        if(total == 0) {
            return;
        }
        JSONArray issues = root.getJSONArray("issues");
        for(int i = 0; i < issues.length(); i++) {
            try {
                JSONObject issue = (JSONObject) issues.get(i);
                if(issue.has("component") && issue.has("textRange")) {
                    String component = issue.getString("component");
                    String filePath = EVALUATION_PATH + sep + component.split(":")[1];
                    JSONObject textRange = (JSONObject) issue.get("textRange");
                    int startLine = textRange.getInt("startLine");
                    int endLine = textRange.getInt("endLine");
                    int startOffset = textRange.getInt("startOffset");
                    int endOffset = textRange.getInt("endOffset");
                    SonarQube_Report report;
                    if (path2report.containsKey(filePath)) {
                        report = path2report.get(filePath);
                    } else {
                        report = new SonarQube_Report(filePath);
                        path2report.put(filePath, report);
                    }
                    SonarQube_Violation violation = new SonarQube_Violation(ruleName, startLine, endLine, startOffset, endOffset);
                    report.addViolation(violation);
                }
            } catch (JSONException e) {
                System.err.println(jsonContent);
                e.printStackTrace();
            }
        }
    }

}