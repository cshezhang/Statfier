package edu.polyu.report;

public class SonarQube_Violation extends Violation {

    private String bugType;
    private int beginLine;

    public SonarQube_Violation(String bugType, int beginLine) {
        this.bugType = bugType;
        this.beginLine = beginLine;
    }

    @Override
    public String getBugType() {
        return null;
    }

    @Override
    public int getBeginLine() {
        return 0;
    }
}
