package edu.polyu.report;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2022/7/28 21:25
 */
public abstract class Violation {

    protected String bugType;
    protected int beginLine;

    public String getBugType() {
        return this.bugType;
    }

    public int getBeginLine() {
        return this.beginLine;
    }

}

