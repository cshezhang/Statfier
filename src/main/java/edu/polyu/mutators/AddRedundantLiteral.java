package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.Util.getChildrenNodes;


/**
 * @Description: The mutant to add redundant literal
 * @Author: Vanguard
 * @Date: 2021-08-20 21:15
 */
public class AddRedundantLiteral extends Mutator {

    private static AddRedundantLiteral addRedundantLiteral = new AddRedundantLiteral();

    private AddRedundantLiteral() {}

    public static AddRedundantLiteral getInstance() {
        return addRedundantLiteral;
    }

    /*
    int a = 10; -> int a = 10 + 1 - 1;
     */

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        List<ASTNode> subNodes = getChildrenNodes(sourceStatement);
        List<NumberLiteral> numberLiterals = new ArrayList<>();
        for(int i = 0; i < subNodes.size(); i++) {
            ASTNode node = subNodes.get(i);
            if(node instanceof NumberLiteral) {
                numberLiterals.add((NumberLiteral) node);
            }
        }
        NumberLiteral targetLiteral = numberLiterals.get(index);
        InfixExpression newLeft = ast.newInfixExpression();
        String value2add;
        if(targetLiteral.getToken().contains(".")) {
            value2add = "1.0";
        } else {
            value2add = "1";
        }
        newLeft.setLeftOperand(ast.newNumberLiteral(value2add));
        newLeft.setOperator(InfixExpression.Operator.PLUS);
        newLeft.setRightOperand((Expression) ASTNode.copySubtree(ast, targetLiteral));
        InfixExpression newRight = ast.newInfixExpression();
        newRight.setLeftOperand(newLeft);
        newRight.setRightOperand(ast.newNumberLiteral(value2add));
        newRight.setOperator(InfixExpression.Operator.MINUS);
        ParenthesizedExpression newRightExpression = ast.newParenthesizedExpression();
        newRightExpression.setExpression(newRight);
        astRewrite.replace(targetLiteral, newRightExpression, null);
        return true;
    }

    @Override
    public int check(Statement statement) {
        // Here, I am not sure whether the condition is too strict
        // Here, we should also consider SimpleName by data flow analysis or type binding.
        int counter = 0;
        List<ASTNode> subNodes = getChildrenNodes(statement);
        for(int i = 0; i < subNodes.size(); i++) {
            ASTNode node = subNodes.get(i);
            if(node instanceof NumberLiteral) {
                counter++;
            }
        }
        return counter;
//        Expression rightValue;
//        if(statement instanceof ExpressionStatement && ((ExpressionStatement) statement).getExpression() instanceof Assignment) {
//            Assignment assignment = (Assignment)((ExpressionStatement) statement).getExpression();
//            String op = assignment.getOperator().toString();
//            if(op.contains("+=") || op.contains("-=") || op.contains("*=") || op.contains("/=")) {
//                rightValue = ((Assignment) ((ExpressionStatement) statement).getExpression()).getRightHandSide();
//                if(rightValue instanceof NumberLiteral) {
//                    return true;
//                }
//            }
//        }
//        if(statement instanceof VariableDeclarationStatement) {
//            rightValue = ((VariableDeclarationFragment)((VariableDeclarationStatement) statement).fragments().get(0)).getInitializer();
//            if(rightValue != null && rightValue instanceof NumberLiteral) {
//                return true;
//            }
//        }
//        return false;
    }
}
