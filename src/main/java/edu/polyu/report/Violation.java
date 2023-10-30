package edu.polyu.report;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2022/7/28 21:25
 */
public interface Violation {

    // Do not need filepath, filepath is saved in Report of Violation
    String getBugType();
    int getBeginLine();

}

