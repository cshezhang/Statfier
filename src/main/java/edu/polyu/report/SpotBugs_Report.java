package edu.polyu.report;

import java.util.List;
import java.util.ArrayList;

/*
 * @Description: 
 * @Author: Austin ZHANG
 * @Date: 2021-10-18 11:21:22
 */

public class SpotBugs_Report {

    private String filename;
    private List<SpotBugs_Violation> violations;
    
    public SpotBugs_Report(String filename) {
        this.filename = filename;
        this.violations = new ArrayList<>();
    }

    public void addViolation(SpotBugs_Violation newViolation) {
        this.violations.add(newViolation);
    }

    public String getFilename() {
        return this.filename;
    }

    public List<SpotBugs_Violation> getViolations() {
        return this.violations;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("SpotBugs Report: " + this.filename + "\n");
        for(int i = 0; i < violations.size(); i++) {
            str.append(violations.get(i) + "\n");
        }
        return str.toString();
    }

}

