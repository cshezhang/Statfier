package edu.polyu;

import edu.polyu.analysis.TypeWrapper;

public class ModuleTester {

    public static void main(String[] args) {
        TypeWrapper wrapper = new TypeWrapper("src/test/java/ASTCase.java", "");
        wrapper.printBasicInfo();
    }

}

