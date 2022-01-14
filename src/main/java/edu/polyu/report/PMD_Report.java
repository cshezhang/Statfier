package edu.polyu.report;

import java.util.ArrayList;

/**
 * Description: PMD report class to save different info in a PMD report
 * Author: Vanguard
 * Date: 2021/9/23 7:30 下午
 */
public class PMD_Report extends Report {

    private String filename;
    private ArrayList<PMD_Violation> violations;

    public PMD_Report(String filename) {
        this.filename = filename;
        this.violations = new ArrayList<>();
    }

    public void addViolation(PMD_Violation pmd_violation) {
        this.violations.add(pmd_violation);
    }

    public String getFilename() {
        return this.filename;
    }

    public ArrayList<PMD_Violation> getViolations() {
        return this.violations;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("PMD_Report Filename: " + this.filename + "\n");
        for(PMD_Violation pmd_violation : this.violations) {
            out.append(pmd_violation.toString() + "\n");
        }
        return out.toString();
    }

}
