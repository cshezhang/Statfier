package edu.polyu.report;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Description:
 * Author: Austin Zhang
 * Date: 2021/9/23 7:30 下午
 */
public class PMD_Violation {

    public int beginLine;
    public int endLine;
    public int beginCol;
    public int endCol;
    public String description;

    PMD_Violation(int beginLine, int endLine, int beginCol, int endCol, String description) {
        this.beginLine = beginLine;
        this.endLine = endLine;
        this.beginCol = beginCol;
        this.endCol = endCol;
        this.description = description;
    }

    public PMD_Violation(JsonNode reportNode) {
        this.beginLine = reportNode.get("beginline").asInt();
        this.beginCol = reportNode.get("begincolumn").asInt();
        this.endLine = reportNode.get("endline").asInt();
        this.endCol = reportNode.get("endcolumn").asInt();
        this.description = reportNode.get("description").toString();
    }

    @Override
    public String toString() {
        return "Violation:" + this.description + " between lines [" + this.beginLine + ", " + this.endLine
                + "], between cols[" + this.beginCol + ", " + this.endCol + "]";
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PMD_Violation) {
            PMD_Violation rhs = (PMD_Violation) o;
            if(rhs.beginLine == this.beginLine && rhs.beginCol == this.beginCol
                    && rhs.endLine == this.endLine && rhs.endCol == this.endCol
                    && rhs.description == this.description) {
                return true;
            }
        }
        return false;
    }

}
