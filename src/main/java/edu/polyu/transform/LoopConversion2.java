package edu.polyu.transform;

import edu.polyu.analysis.LoopStatement;
import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2022/9/1 19:19
 */
public class LoopConversion2 extends Transform {

    private static LoopConversion2 loopConversion2 = new LoopConversion2();

    private LoopConversion2() {
    }

    public static LoopConversion2 getInstance() {
        return loopConversion2;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode broNode, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        WhileStatement oldStatement = (WhileStatement) targetNode;
        LoopStatement loopStatement = new LoopStatement(oldStatement);
        if(!loopStatement.isReachable()) {
            return false;
        }
        Expression oldCondition = oldStatement.getExpression();
        Expression newCondition = (Expression) ASTNode.copySubtree(ast, oldCondition);
        Statement newBody = (Statement) ASTNode.copySubtree(ast, oldStatement.getBody());
        DoStatement newStatement = ast.newDoStatement();
        newStatement.setExpression(newCondition);
        newStatement.setBody(newBody);
        astRewrite.replace(oldStatement, newStatement, null);
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        while (true) {
            if (node == null || node instanceof FieldDeclaration || node instanceof MethodDeclaration || node instanceof TypeDeclaration) {
                break;
            }
            if (node instanceof WhileStatement) {
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
