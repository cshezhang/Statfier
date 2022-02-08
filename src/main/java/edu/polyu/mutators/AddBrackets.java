package edu.polyu.mutators;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/*
 * @Description:
 * @Author: Vanguard
 * @Date: 2021-10-14 09:25:07
 */
public class AddBrackets extends Mutator {

    private static AddBrackets addBrackets = new AddBrackets();

    private AddBrackets() {}

    public static AddBrackets getInstance() {
        return addBrackets;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
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
    public int check(Statement statement) {
        if(statement instanceof ExpressionStatement) {
            Expression expression = ((ExpressionStatement)statement).getExpression();
            // https://www.ibm.com/docs/en/rational-soft-arch/9.5?topic=r-api-reference-1
            // Actually, we only need to consider Assignment, others can be dismissed
            return expression instanceof Assignment ? 1 : 0;
        }
        if(statement instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) statement;
            if(vdStatement.getType() instanceof ArrayType) {
                return 0;
            }
            VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
            if(vdFragment.getInitializer() != null) {
                return 1;
            }
        }
        return 0;
    }
}
