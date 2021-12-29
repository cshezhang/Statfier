/*
 * @Description: 
 * @Author: Austin ZHANG
 * @Date: 2021-10-14 09:25:07
 */
package edu.polyu.mutators;


import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

public class AddBrackets extends Mutator {

    private static AddBrackets addBrackets = new AddBrackets();

    private AddBrackets() {}

    public static AddBrackets getInstance() {
        return addBrackets;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        Expression expression = null;
        if(sourceStatement instanceof ExpressionStatement) {
            expression = ((ExpressionStatement) sourceStatement).getExpression();
            if(expression instanceof Assignment) {
                expression = ((Assignment)expression).getRightHandSide();
            }
        }
        if(sourceStatement instanceof VariableDeclarationStatement) {
            VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) sourceStatement).fragments().get(0);
            expression = vdFragment.getInitializer();
        }
        ParenthesizedExpression parenthesizedExpression = ast.newParenthesizedExpression();
        Expression content = (Expression) ASTNode.copySubtree(ast, expression);
        parenthesizedExpression.setExpression(content);
        astRewrite.replace(expression, parenthesizedExpression, null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        if(statement instanceof ExpressionStatement) {
            Expression expression = ((ExpressionStatement)statement).getExpression();
            // https://www.ibm.com/docs/en/rational-soft-arch/9.5?topic=r-api-reference-1
            // 事实证明只有Assignment能用，别的expression不用考虑
            return expression instanceof Assignment ? true : false;
        }
        if(statement instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) statement;
            if(vdStatement.getType() instanceof ArrayType) {
                return false;
            }
            VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
            if(vdFragment.getInitializer() != null) {
                return true;
            }
        }
        return false;
    }
}
