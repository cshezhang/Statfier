/*
 * @Description: 
 * @Author: Austin ZHANG
 * @Date: 2021-10-14 09:25:07
 */
package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import static edu.polyu.Util.random;

/**
 * @Description: The mutant to add redundant literal
 * @Author: Huaien Zhang
 * @Date: 2021-08-20 21:15
 */
public class AddRedundantLiteral extends Mutator {

    private static AddRedundantLiteral addRedundantLiteral = new AddRedundantLiteral();

    private AddRedundantLiteral() {}

    public static AddRedundantLiteral getInstance() {
        return addRedundantLiteral;
    }

    @Override
    public int getIndex() {
        return 6;
    }

    /*
    int a = 10; -> int a = 10 + 1 - 1;
     */

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        Expression oldRightValue = null;
        if(sourceStatement instanceof ExpressionStatement && ((ExpressionStatement) sourceStatement).getExpression() instanceof Assignment) {
            oldRightValue = ((Assignment) ((ExpressionStatement) sourceStatement).getExpression()).getRightHandSide();
        }
        if(sourceStatement instanceof VariableDeclarationStatement) {
            oldRightValue = ((VariableDeclarationFragment)((VariableDeclarationStatement) sourceStatement).fragments().get(0)).getInitializer();
        }
        InfixExpression newRightValue = ast.newInfixExpression();
        newRightValue.setOperator(InfixExpression.Operator.MINUS);
        String str = sourceStatement.toString();
        String value2add;
        if(str.contains("float") || str.contains("double")) {
            value2add = String.format("%f", 100 * Math.random());
        } else {
            value2add = String.format("%d", (int)(100 * Math.random()));
        }
        newRightValue.setRightOperand(ast.newNumberLiteral(value2add));
        InfixExpression newLeftInRight = ast.newInfixExpression();
        newLeftInRight.setLeftOperand((Expression) ASTNode.copySubtree(ast, oldRightValue));
        newLeftInRight.setOperator(InfixExpression.Operator.PLUS);
        newLeftInRight.setRightOperand(ast.newNumberLiteral(value2add.toString()));
        newRightValue.setLeftOperand(newLeftInRight);
        astRewrite.replace(oldRightValue, newRightValue, null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        // Here, I am not sure whether the condition is too strict
        // Here, we should also consider SimpleName by data flow analysis or type binding.
        Expression rightValue;
        if(statement instanceof ExpressionStatement && ((ExpressionStatement) statement).getExpression() instanceof Assignment) {
            Assignment assignment = (Assignment)((ExpressionStatement) statement).getExpression();
            String op = assignment.getOperator().toString();
            if(op.contains("+=") || op.contains("-=") || op.contains("*=") || op.contains("/=")) {             
                rightValue = ((Assignment) ((ExpressionStatement) statement).getExpression()).getRightHandSide();
                if(rightValue instanceof NumberLiteral) {
                    return true;
                }
            }
        }
        if(statement instanceof VariableDeclarationStatement) {
            rightValue = ((VariableDeclarationFragment)((VariableDeclarationStatement) statement).fragments().get(0)).getInitializer();
            if(rightValue != null && rightValue instanceof NumberLiteral) {
                return true;
            }
        }
        return false;
    }
}
