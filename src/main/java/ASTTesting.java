import edu.polyu.analysis.ASTWrapper;
import edu.polyu.util.Util;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

import java.util.ArrayList;
import java.util.List;

public class ASTTesting {

    public static void main(String[] args) {
        String path = "src/test/java/CaseTest.java";
        ASTWrapper wrapper = new ASTWrapper(path, "evaluation");
        wrapper.printBasicInfo();
        for(ASTNode node : wrapper.getAllNodes()) {
            if(node instanceof SwitchStatement) {
                List<ASTNode> nodes = new ArrayList<>();
                for(ASTNode subNode : Util.getChildrenNodes(node)) {
                    if(Util.checkExpressionLiteral(subNode)) {
                        nodes.add(subNode);
                    }
                }
                for(Statement statement : (List<Statement>) ((SwitchStatement) node).statements()) {
                    if(statement instanceof SwitchCase) {
                        for(ASTNode literal : nodes) {
                            Expression e = ((SwitchCase) statement).getExpression();
                            if(literal == e) {
                                System.out.println("get");
                            }
                        }
                    }
                }
            }
        }
    }

}
