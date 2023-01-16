package org.detector.transform;


import org.detector.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static org.detector.analysis.TypeWrapper.getChildrenNodes;

public class CompoundExpression2 extends Transform {

    private static final CompoundExpression2 instance = new CompoundExpression2();

    private CompoundExpression2() {
    }

    public static CompoundExpression2 getInstance() {
        return instance;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        List<ASTNode> subNodes = TypeWrapper.getChildrenNodes(node);
        for (ASTNode subNode : subNodes) {
            if (subNode instanceof BooleanLiteral) {
                nodes.add(subNode);
            }
        }
        return nodes;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brother, ASTNode sourceStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        BooleanLiteral targetLiteral = (BooleanLiteral) targetNode;
        ParenthesizedExpression newParenthesizedExpression = ast.newParenthesizedExpression();
        InfixExpression newInfixExpression = ast.newInfixExpression();
        newInfixExpression.setLeftOperand((BooleanLiteral) ASTNode.copySubtree(ast, targetLiteral));
        if (targetLiteral.booleanValue()) {
            newInfixExpression.setRightOperand(ast.newBooleanLiteral(false));
            newInfixExpression.setOperator(InfixExpression.Operator.OR);
        } else {
            newInfixExpression.setRightOperand(ast.newBooleanLiteral(true));
            newInfixExpression.setOperator(InfixExpression.Operator.AND);
        }
        newParenthesizedExpression.setExpression(newInfixExpression);
        astRewrite.replace(targetLiteral, newParenthesizedExpression, null);
        return true;
    }

}
