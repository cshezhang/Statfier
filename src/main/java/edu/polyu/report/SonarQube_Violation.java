package edu.polyu.report;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/8/29 15:22
 */
public class SonarQube_Violation extends Violation {

    private String bugType;
    private int beginLine;

    public SonarQube_Violation(String bugType, int beginLine) {
        this.bugType = bugType;
        this.beginLine = beginLine;
    }

    public String getBugType() {
        return this.bugType;
    }

    public int getBeginLine() {
        return this.beginLine;
    }

}
