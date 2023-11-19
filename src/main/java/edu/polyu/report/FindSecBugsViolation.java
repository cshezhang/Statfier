package edu.polyu.report;

import org.dom4j.Element;

import java.io.File;

/**
 * @Intro: Save a bug instance in report.
 * @Author: RainyD4y
 * @Date: 2021-10-30 15:08:30
 */
public class FindSecBugsViolation extends Violation {

    private int beginLine;
    private int endLine;
    private String bugType;

    public FindSecBugsViolation(Element sourceLine, String bugType) {
        if(sourceLine.attribute("start") != null) {
            this.beginLine = Integer.parseInt(sourceLine.attribute("start").getText());
        } else {
            this.beginLine = -1;
        }
        if(sourceLine.attribute("end") != null) {
            this.endLine = Integer.parseInt(sourceLine.attribute("end").getText());
        } else {
            this.endLine = -1;
        }
        this.bugType = bugType; // Consider the category attribute of BugInstance element
    }

    public int getBeginLine() {
        return this.beginLine;
    }

    public String getBugType() {
        return this.bugType;
    }

    @Override
    public String toString() {
        return "[" + this.bugType + "] between [" + this.beginLine + ", " + this.endLine + "]";
    }

}