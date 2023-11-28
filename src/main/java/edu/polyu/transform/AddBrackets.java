package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

public class AddBrackets extends Transform {

    private static AddBrackets addBrackets = new AddBrackets();

    private AddBrackets() {}

    public static AddBrackets getInstance() {
        return addBrackets;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brotherStatement, ASTNode sourceStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
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
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode statement) {
        List<ASTNode> nodes = new ArrayList<>();
        if(statement instanceof ExpressionStatement) {
            Expression expression = ((ExpressionStatement)statement).getExpression();
            if(expression instanceof Assignment) {
                nodes.add(statement);
            }
        } else {
            if (statement instanceof VariableDeclarationStatement) {
                VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) statement;
                if (vdStatement.getType() instanceof ArrayType) {
                    return nodes;
                }
                VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
                if (vdFragment.getInitializer() != null) {
                    nodes.add(statement);
                }
            }
        }
        return nodes;
    }
}
