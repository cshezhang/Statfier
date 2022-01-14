package edu.polyu.report;

import java.util.ArrayList;
import java.util.List;

public class SonarQube_Report extends Report {

    private String fileName;
    private List<SonarQube_Violation> violations;

    public SonarQube_Report(String fileName) {
        this.fileName = fileName;
        this.violations = new ArrayList<>();
    }

    public void addViolation(SonarQube_Violation violation) {
        this.violations.add(violation);
    }

    public String getFileName() {
        return this.fileName;
    }

    public List<SonarQube_Violation> getViolations() {
        return this.violations;
    }

}
