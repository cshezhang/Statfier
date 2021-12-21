package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.Locale;

import static edu.polyu.Util.*;

/**
 * @Description: Add assignment mutatant
 * @Author: Huaien Zhang
 * @Date: 2021-10-05 12:49
 */
public class AddArgAssignment extends Mutator {

    private static AddArgAssignment addArgAssignment = new AddArgAssignment();

    private AddArgAssignment() {}

    public static Mutator getInstance() {
        return addArgAssignment;
    }

    @Override
    public int getIndex() {
        return 1;
    }

    /**
     * int a = constant; -> final int b = constant; int a = b;
     * Var A = NEW B(xxx, yyy); => n = xxx; Var A = NEW B(n, yyy);
     * Similary, ExpressionStatement: MethodInvocation, Assignment
     * Notice: New Assignment should be added final modifier
     */
    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        Expression targetExpression = null, rightExpression = null;
        if(sourceStatement instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) sourceStatement;
            rightExpression = ((VariableDeclarationFragment) vdStatement.fragments().get(0)).getInitializer();
        }
        if(sourceStatement instanceof ExpressionStatement) {
            Expression expression = ((ExpressionStatement) sourceStatement).getExpression();
            if(expression instanceof Assignment) {
                rightExpression = ((Assignment) expression).getRightHandSide();
            }
            if(expression instanceof MethodInvocation) {
                rightExpression = expression;
            }
        }
        for(ASTNode astNode : getChildrenNodes(rightExpression)) {
            if(astNode instanceof Expression && checkExpressionLiteral((Expression)astNode)) {
                targetExpression = (Expression) astNode;
            }
        }
        if(targetExpression == null || targetExpression instanceof NullLiteral) {
            return false;
        }
        SimpleName newVarName = ast.newSimpleName("var" + newVarCounter++);
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        newVdFragment.setName(newVarName);
        newVdFragment.setInitializer((Expression)ASTNode.copySubtree(ast, targetExpression));
        VariableDeclarationStatement newVdStatement = ast.newVariableDeclarationStatement(newVdFragment);
        newVdStatement.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
        // 这里可能考虑的还不是很全面
//        if(targetExpression instanceof NullLiteral) { // Here is removed because it is a little difficult to get the type info of this parameter
//            newVdStatement.setType(ast.newSimpleType(ast.newSimpleName("Object")));
//        }
        if(targetExpression instanceof StringLiteral) {
            newVdStatement.setType(ast.newSimpleType(ast.newSimpleName("String")));
        }
        if(targetExpression instanceof NumberLiteral) {
            String value = ((NumberLiteral) targetExpression).getToken();
            if(value.contains(".")) {
                newVdStatement.setType(ast.newPrimitiveType(PrimitiveType.DOUBLE));
            } else {
                if(value.endsWith("L")) {
                    newVdStatement.setType(ast.newPrimitiveType(PrimitiveType.LONG));
                } else {
                    value = value.toLowerCase();
                    long longValue;
                    if(value.contains("0x") || value.contains("0X") || value.contains("a") || value.contains("b") || value.contains("c") || value.contains("d") || value.contains("e") ||
                            value.contains("f")) {
                        longValue = Long.parseLong(value, 16);
                    } else {
                        longValue = Long.parseLong(value);
                    }
                    if (longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE) {
                        newVdStatement.setType(ast.newPrimitiveType(PrimitiveType.LONG));
                    } else {
                        newVdStatement.setType(ast.newPrimitiveType(PrimitiveType.INT));
                    }
                }
            }
        }
        if(targetExpression instanceof BooleanLiteral) {
            newVdStatement.setType(ast.newPrimitiveType(PrimitiveType.BOOLEAN));
        }
        if(targetExpression instanceof CharacterLiteral) {
            newVdStatement.setType(ast.newPrimitiveType(PrimitiveType.CHAR));
        }
        try {
            astRewrite.replace(targetExpression, newVarName, null);
        } catch (Exception e) {
            System.err.println("Target Expression: " + targetExpression);
            System.err.println("newVar: " + newVarName);
            e.printStackTrace();
        }
        ListRewrite listRewrite = astRewrite.getListRewrite(brotherStatement.getParent(), Block.STATEMENTS_PROPERTY);
        try {
            listRewrite.insertBefore(newVdStatement, brotherStatement, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
            Expression rightExpression = vdFragment.getInitializer();
            if(rightExpression instanceof MethodInvocation || rightExpression instanceof ClassInstanceCreation) {
                return true;
            }
        }
        if(statement instanceof ExpressionStatement) {
            Expression expression = ((ExpressionStatement) statement).getExpression();
            if(expression instanceof MethodInvocation || expression instanceof Assignment) {
                return true;
            }
        }
        return false;
    }
}
