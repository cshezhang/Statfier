package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.analysis.TypeWrapper.getChildrenNodes;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2022/9/2 17:09
 */
public class CompoundExpression5 extends Transform {

    private static CompoundExpression5 instance = new CompoundExpression5();

    private CompoundExpression5() {}

    public static CompoundExpression5 getInstance() {
        return instance;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        List<ASTNode> subNodes = TypeWrapper.getChildrenNodes(node);
        for (ASTNode subNode : subNodes) {
            if (subNode instanceof InfixExpression) {
                if(((InfixExpression) subNode).getOperator().toString().equals("==")) {
                    nodes.add(subNode);
                }
            }
        }
        return nodes;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode broNode, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        InfixExpression oldInfixExpression = (InfixExpression) targetNode;
        InfixExpression newInfixExpression = ast.newInfixExpression();
        newInfixExpression.setLeftOperand((Expression) ASTNode.copySubtree(ast, oldInfixExpression.getRightOperand()));
        newInfixExpression.setRightOperand((Expression) ASTNode.copySubtree(ast, oldInfixExpression.getLeftOperand()));
        astRewrite.replace(oldInfixExpression, newInfixExpression, null);
        return true;
    }

    @Override
    public String getIndex() {
        return "CompoundExpression5";
    }
}
