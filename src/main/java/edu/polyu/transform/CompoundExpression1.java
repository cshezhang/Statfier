package edu.polyu.transform;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Util.getChildrenNodes;

public class CompoundExpression1 extends Transform {

    private static final CompoundExpression1 instance = new CompoundExpression1();

    private CompoundExpression1() {}

    public static CompoundExpression1 getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brother, ASTNode oldStatement) {
        List<ASTNode> nodes = getChildrenNodes(oldStatement);
        List<BooleanLiteral> boolLiterals = new ArrayList<>();
        for(ASTNode node : nodes) {
            if(node instanceof BooleanLiteral) {
                boolLiterals.add((BooleanLiteral) node);
            }
        }
        BooleanLiteral targetLiteral = boolLiterals.get(index);
        ParenthesizedExpression newParenthesizedExpression = ast.newParenthesizedExpression();
        InfixExpression newInfixExpression = ast.newInfixExpression();
        newInfixExpression.setLeftOperand((BooleanLiteral) ASTNode.copySubtree(ast, targetLiteral));
        if(targetLiteral.booleanValue()) {
            newInfixExpression.setRightOperand(ast.newBooleanLiteral(false));
            newInfixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_OR);
        } else {
            newInfixExpression.setRightOperand(ast.newBooleanLiteral(true));
            newInfixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_AND);
        }
        newParenthesizedExpression.setExpression(newInfixExpression);
        astRewrite.replace(targetLiteral, newParenthesizedExpression, null);
        return true;
    }

    @Override
    public int check(ASTNode statement) {
        int counter = 0;
        List<ASTNode> nodes = getChildrenNodes(statement);
        for(ASTNode node : nodes) {
            if(node instanceof BooleanLiteral) {
                counter++;
            }
        }
        return counter;
    }
}
