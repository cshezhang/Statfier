package edu.polyu;

import edu.polyu.analysis.TypeWrapper;

public class ASTTesting {

    public static void main(String[] args) {
        String path = "src/test/java/CaseTest.java";
        TypeWrapper wrapper = new TypeWrapper(path, "evaluation");
        wrapper.printBasicInfo();
    }

}
