package edu.polyu.report;

import org.dom4j.Element;

import java.io.File;

/**
 * @Intro: Save a bug instance in report.
 * @Author: RainyD4y
 * @Date: 2021-10-30 15:08:30
 */
public class FindSecBugs_Violation implements Violation {

    private String filepath;
    private int beginLine;
    private int endLine;
    private String bugType;

    public FindSecBugs_Violation(String seedFolderPath, Element sourceLine, String bugType) {
        this.filepath = seedFolderPath  + File.separator + sourceLine.attribute("sourcefile").getText();
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

    public String getFilepath() {
        return this.filepath;
    }

    public int getBeginLine() {
        return this.beginLine;
    }

    public String getBugType() {
        return this.bugType;
    }

    @Override
    public String toString() {
        return this.filepath + ": [" + this.bugType + "] between [" + this.beginLine + ", " + this.endLine + "]";
    }

}