package edu.polyu.report;

import org.dom4j.Element;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/2/4 1:23 下午
 */
public class Infer_Violation extends Violation {

    private int beginLine;
    private int endLine;
    private String filename;
    private String bugType;


    public Infer_Violation(String filename, Element violation) {
        this.beginLine = Integer.parseInt(violation.attribute("beginline").getText());
        this.endLine = Integer.parseInt(violation.attribute("endline").getText());
        this.filename = filename;
        this.bugType = violation.attribute("rule").getText();
    }

    public String getFilename() {
        return this.filename;
    }

    @Override
    public String getBugType() {
        return null;
    }

    @Override
    public int getBeginLine() {
        return 0;
    }

    public int getEndLine() {
        return this.endLine;
    }
}
