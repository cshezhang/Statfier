package edu.polyu.report;

public class CheckStyleViolation extends Violation {

    private String fileName;

    public CheckStyleViolation(String fileName, int beginLine, String bugType) {
        this.fileName = fileName;
        this.beginLine = beginLine;
        this.bugType = bugType;
    }

    public String getFileName() {
        return this.fileName;
    }

    @Override
    public String toString() {
        return "File: " + this.fileName + " Line: " + this.beginLine + " Type: " + this.bugType;
    }

}
