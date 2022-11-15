package org.rainyday.report;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/2/4 1:23 PM
 */
public class Infer_Violation extends Violation {

    private int row;
    private String bugType;


    public Infer_Violation(JsonNode violation) {
        this.row = violation.get("line").asInt();
        this.bugType = violation.get("bug_type").asText();
    }

    public String getBugType() {
        return this.bugType;
    }

    public int getBeginLine() {
        return this.row;
    }

}
