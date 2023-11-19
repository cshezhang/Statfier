package edu.polyu.report;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2021/9/23 7:30 PM
 */
public class PMDViolation extends Violation {

    public int endLine;
    public int beginCol;
    public int endCol;
    public String description;

    public PMDViolation(int beginLine, int endLine, int beginCol, int endCol, String bugType) {
        this.beginLine = beginLine;
        this.endLine = endLine;
        this.beginCol = beginCol;
        this.endCol = endCol;
        this.bugType = bugType;
    }

    public PMDViolation(JsonNode reportNode) {
        this.beginLine = reportNode.get("beginline").asInt();
        this.endLine = reportNode.get("endline").asInt();
        this.beginCol = reportNode.get("begincolumn").asInt() - 1;
        this.endCol = reportNode.get("endcolumn").asInt() + 1;
        this.bugType = reportNode.get("rule").asText();
        this.description = reportNode.get("ruleset") + ":[" + reportNode.get("rule") + ", " + reportNode.get("description").toString() + "]";
    }

    public String getBugType() {
        return this.bugType;
    }

    public int getBeginLine() {
        return this.beginLine;
    }

    public int getBeginCol() {
        return beginCol;
    }

    public int getEndCol() {
        return endCol;
    }

    @Override
    public String toString() {
        return "Violation:" + this.bugType + " start at line [" + this.beginLine + "]";
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PMDViolation) {
            PMDViolation rhs = (PMDViolation) o;
            if(rhs.beginLine == this.beginLine && rhs.beginCol == this.beginCol
                    && rhs.endLine == this.endLine && rhs.endCol == this.endCol
                    && rhs.bugType == this.bugType) {
                return true;
            }
        }
        return false;
    }

}
