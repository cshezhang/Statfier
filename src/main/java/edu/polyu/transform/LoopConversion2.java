package edu.polyu.transform;

import edu.polyu.analysis.LoopStatement;
import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/9/1 19:19
 */
public class LoopConversion2 extends Transform {

    private static LoopConversion2 loopConversion2 = new LoopConversion2();

    private LoopConversion2() {}

    public static LoopConversion2 getInstance() {
        return loopConversion2;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode broNode, ASTNode srcNode) {
        if(srcNode instanceof )
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        while(true) {
            if(node == null || node instanceof FieldDeclaration || node instanceof MethodDeclaration || node instanceof TypeDeclaration) {
                break;
            }
            if (LoopStatement.isLoopStatement(node)) {
                nodes.add(node);
            }
            node = node.getParent();
        }
        return nodes;
    }

    @Override
    public String getIndex() {
        return "LoopConversion2";
    }
}
