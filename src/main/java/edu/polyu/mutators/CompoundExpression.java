package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.Util.getChildrenNodes;
import static edu.polyu.Util.random;

public class CompoundExpression extends Mutator {

    private static final CompoundExpression instance = new CompoundExpression();

    private CompoundExpression() {}

    public static CompoundExpression getInstance() {
        return instance;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        List<ASTNode> nodes = getChildrenNodes(sourceStatement);
        List<BooleanLiteral> boolLiterals = new ArrayList<>();
        for(ASTNode node : nodes) {
            if(node instanceof BooleanLiteral) {
                boolLiterals.add((BooleanLiteral) node);
            }
        }
        int randomIndex = random.nextInt(boolLiterals.size());
        BooleanLiteral targetLiteral = boolLiterals.get(randomIndex);
        ParenthesizedExpression newParenthesizedExpression = ast.newParenthesizedExpression();
        InfixExpression newInfixExpression = ast.newInfixExpression();
        newInfixExpression.setLeftOperand((BooleanLiteral) ASTNode.copySubtree(ast, targetLiteral));
        if(Math.random() < 0.5) {
            newInfixExpression.setOperator(InfixExpression.Operator.OR);
            newInfixExpression.setRightOperand(ast.newBooleanLiteral(false));
        } else {
            newInfixExpression.setOperator(InfixExpression.Operator.AND);
            newInfixExpression.setRightOperand(ast.newBooleanLiteral(true));
        }
        newParenthesizedExpression.setExpression(newInfixExpression);
        astRewrite.replace(targetLiteral, newParenthesizedExpression, null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        List<ASTNode> nodes = getChildrenNodes(statement);
        for(ASTNode node : nodes) {
            if(node instanceof BooleanLiteral) {
                return true;
            }
        }
        return false;
    }
}
