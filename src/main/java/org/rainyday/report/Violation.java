package org.rainyday.report;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/7/28 21:25
 */
public abstract class Violation {

    // Do not need filepath, filepath is saved in Report

    public abstract String getBugType();

    public abstract int getBeginLine();

}
