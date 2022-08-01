package edu.polyu;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.util.Utility;

public class ASTTesting {


    public static void main(String[] args) {
        TypeWrapper wrapper = new TypeWrapper(Utility.AST_TESTING_PATH, "code");
        wrapper.printBasicInfo();
    }

}
