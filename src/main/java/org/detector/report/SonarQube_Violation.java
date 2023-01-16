package org.detector.report;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/8/29 15:22
 */
public class SonarQube_Violation implements Violation {

    private String bugType;
    private int beginLine;
    private int endLine;
    private int startOffset;
    private int endOffset;

    public SonarQube_Violation(String bugType, int beginLine, int endLine, int startOffset, int endOffset) {
        this.bugType = bugType;
        this.beginLine = beginLine;
        this.endLine = endLine;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    @Override
    public String getBugType() {
        return this.bugType;
    }

    @Override
    public int getBeginLine() {
        return this.beginLine;
    }

    @Override
    public String toString() {
        return "Bug Type: " + this.bugType + " Line[" + this.beginLine + ", " + this.endLine + "], Offset[" + this.startOffset + ", " + this.endOffset + "]";
    }

    @Override
    public int hashCode() {
        return this.bugType.hashCode() + this.beginLine + this.endLine + this.startOffset + this.endOffset;
    }

    @Override
    public boolean equals(Object rhs) {
        if(rhs instanceof SonarQube_Report) {
            if(this.hashCode() == rhs.hashCode()) {
                return true;
            }
        }
        return false;
    }

}
