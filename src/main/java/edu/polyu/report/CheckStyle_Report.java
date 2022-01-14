package edu.polyu.report;

import java.util.ArrayList;
import java.util.List;

public class CheckStyle_Report extends Report {

    private String fileName;
    private List<CheckStyle_Violation> violations;

    public CheckStyle_Report(String fileName) {
        this.fileName = fileName;
        this.violations = new ArrayList<>();
    }

    public void addViolation(CheckStyle_Violation violation) {
        this.violations.add(violation);
    }

    public String getFileName() {
        return this.fileName;
    }

    public List<CheckStyle_Violation> getViolations() {
        return this.violations;
    }

}
