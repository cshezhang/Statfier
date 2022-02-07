package edu.polyu.mutators;

<<<<<<< HEAD
import edu.polyu.Mutator;
=======
>>>>>>> d2a3d5d792e1b70378656198bac2f3a1c133ad84
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

public class CompoundExpression2 extends Mutator {

    private static final CompoundExpression2 instance = new CompoundExpression2();

    private CompoundExpression2() {}

    public static CompoundExpression2 getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        List<ASTNode> nodes = getChildrenNodes(sourceStatement);
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
            newInfixExpression.setOperator(InfixExpression.Operator.OR);
        } else {
            newInfixExpression.setRightOperand(ast.newBooleanLiteral(true));
            newInfixExpression.setOperator(InfixExpression.Operator.AND);
        }
        newParenthesizedExpression.setExpression(newInfixExpression);
        astRewrite.replace(targetLiteral, newParenthesizedExpression, null);
        return true;
    }

    @Override
    public int check(Statement statement) {
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
