package edu.polyu.report;


/**
 * Description:
 * Author: RainyD4y
 * Date: 2022/2/4 1:23 PM
 */
public class InferViolation extends Violation {


    public InferViolation(int beginLine, String bugType) {
        this.beginLine = beginLine;
        this.bugType = bugType;
    }

}
