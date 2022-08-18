package edu.polyu.report;

import com.fasterxml.jackson.databind.JsonNode;

public class SonarQube_Violation extends Violation {

    private String bugType;
    private int beginLine;

    public SonarQube_Violation(JsonNode issueNode) {
        this.bugType = issueNode.get("rule").asText();
        this.beginLine = issueNode.get("textRange").get("startLine").asInt();
    }

    public String getBugType() {
        return this.bugType;
    }

    public int getBeginLine() {
        return this.beginLine;
    }

}
