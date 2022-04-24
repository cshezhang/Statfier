import edu.polyu.analysis.ASTWrapper;

public class ASTTesting {

    public static void main(String[] args) {
        String path = "src/test/java/CaseTest.java";
        ASTWrapper wrapper = new ASTWrapper(path, "evaluation");
        wrapper.printBasicInfo();
    }

}
