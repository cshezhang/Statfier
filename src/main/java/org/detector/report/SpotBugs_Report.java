package org.detector.report;

import java.util.List;
import java.util.ArrayList;

/**
 * @Description: 
 * @Author: Vanguard
 * @Date: 2021-10-18 11:21:22
 */
public class SpotBugs_Report implements Report {

    private String filepath;
    private List<SpotBugs_Violation> violations;
    
    public SpotBugs_Report(String filepath) {
        this.filepath = filepath;
        this.violations = new ArrayList<>();
    }

    public void addViolation(SpotBugs_Violation newViolation) {
        this.violations.add(newViolation);
    }

    public String getFilepath() {
        return this.filepath;
    }

    public List<SpotBugs_Violation> getViolations() {
        return this.violations;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("SpotBugs Report: " + this.filepath + "\n");
        for(int i = 0; i < violations.size(); i++) {
            str.append(violations.get(i) + "\n");
        }
        return str.toString();
    }

}

