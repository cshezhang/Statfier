package org.detector.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: PMD report class to save different info in a PMD report
 * Author: Vanguard
 * Date: 2021/9/23 7:30
 */
public class PMD_Report implements Report {

    private String filepath;
    private List<PMD_Violation> violations;

    public PMD_Report(String filepath) {
        this.filepath = filepath;
        this.violations = new ArrayList<>();
    }

    public void addViolation(PMD_Violation pmd_violation) {
        this.violations.add(pmd_violation);
    }

    public String getFilepath() {
        return this.filepath;
    }

    public List<PMD_Violation> getViolations() {
        return this.violations;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("PMD_Report Filename: " + this.filepath + "\n");
        for(PMD_Violation pmd_violation : this.violations) {
            out.append(pmd_violation.toString() + "\n");
        }
        return out.toString();
    }

}
