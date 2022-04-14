package edu.polyu.report;

public abstract class Violation {

    // Do not need filepath, filepath is saved in Report

    public abstract String getBugType();

    public abstract int getBeginLine();

}
