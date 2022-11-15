package org.rainyday.report;

import java.util.ArrayList;
import java.util.List;

public class Infer_Report extends Report {

    private String filepath;
    private List<Infer_Violation> violations;

    public Infer_Report(String filepath) {
        this.filepath = filepath;
        this.violations = new ArrayList<>();
    }

    public List<Infer_Violation> getViolations() {
        return this.violations;
    }

    public void addViolation(Infer_Violation violation) {
        this.violations.add(violation);
    }

    public String getFilepath() {
        return this.filepath;
    }

}
