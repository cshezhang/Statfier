package org.rainyday.report;

import java.util.ArrayList;
import java.util.List;

public class CheckStyle_Report extends Report {

    private String filepath;
    private List<CheckStyle_Violation> violations;

    public CheckStyle_Report(String filepath) {
        this.filepath = filepath;
        this.violations = new ArrayList<>();
    }

    public void addViolation(CheckStyle_Violation violation) {
        this.violations.add(violation);
    }

    public String getFilepath() {
        return this.filepath;
    }

    public List<CheckStyle_Violation> getViolations() {
        return this.violations;
    }

}
