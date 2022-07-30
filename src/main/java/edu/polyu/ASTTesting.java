package edu.polyu;

import edu.polyu.analysis.TypeWrapper;

public class ASTTesting {


    public static void main(String[] args) {
        TypeWrapper wrapper = new TypeWrapper("/Users/austin/code/Test.java", "code");
        wrapper.printBasicInfo();
    }

}
