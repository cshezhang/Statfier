package edu.polyu.report;

import java.util.ArrayList;
import java.util.List;

public class Infer_Report extends Report {

    private String filename;
    private List<Infer_Violation> violations;

    public Infer_Report(String filename) {
        this.filename = filename;
        this.violations = new ArrayList<>();
    }

    public List<Infer_Violation> getViolations() {
        return this.violations;
    }

    public void addViolation(Infer_Violation violation) {
        this.violations.add(violation);
    }

    public String getFilename() {
        return this.filename;
    }

}
