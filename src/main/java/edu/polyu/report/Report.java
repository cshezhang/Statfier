package edu.polyu.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2022/7/28 21:22
 */
public abstract class Report {

    protected String filePath;
    protected List<Violation> violations;

    public Report(String filePath) {
        this.filePath = filePath;
        this.violations = new ArrayList<>();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void addViolation(Violation violation) {
        this.violations.add(violation);
    }

    public List<Violation> getViolations() {
        return this.violations;
    }

}