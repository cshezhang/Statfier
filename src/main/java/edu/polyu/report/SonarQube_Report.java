package edu.polyu.report;

import java.util.ArrayList;
import java.util.List;

public class SonarQube_Report extends Report {

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

    @Override
    public String getFilepath() {
        return this.filepath;
    }

}
