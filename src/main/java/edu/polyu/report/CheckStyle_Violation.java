package edu.polyu.report;

public class CheckStyle_Violation extends Violation {

    private String fileName;
    private String bugType;
    private int beginLine;
    private int beginColumn;

    public CheckStyle_Violation(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getBugType() {
        return this.bugType;
    }

    public int getBeginLine() {
        return this.beginLine;
    }

    public void setBugType(String bugType) {
        this.bugType = bugType;
    }

    public void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    public void setBeginColumn(int beginColumn) {
        this.beginColumn = beginColumn;
    }

    @Override
    public String toString() {
        return "File: " + this.fileName + " Line: " + this.beginLine + " Type: " + this.bugType;
    }
}
