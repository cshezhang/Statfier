package edu.polyu.report;

import com.fasterxml.jackson.databind.JsonNode;
import org.dom4j.Element;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/2/4 1:23 PM
 */
public class Infer_Violation extends Violation {

    private int row;
    private int col;
    private String bugType;


    public Infer_Violation(JsonNode violation) {
        this.row = violation.get("line").asInt();
        this.col = violation.get("column").asInt();
        this.bugType = violation.get("bug_type").asText();
    }

    @Override
    public String getBugType() {
        return this.bugType;
    }

    @Override
    public int getBeginLine() {
        return this.row;
    }

    public int getColNumber() {
        return this.col;
    }

}
