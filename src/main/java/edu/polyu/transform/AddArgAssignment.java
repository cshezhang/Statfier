package edu.polyu.transform;

import edu.polyu.analysis.ASTWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Util.*;

/**
 * @Description: Add assignment mutatant
 * @Author: Vanguard
 * @Date: 2021-10-05 12:49
 */
public class AddArgAssignment extends Transform {

    private static AddArgAssignment addArgAssignment = new AddArgAssignment();

    private AddArgAssignment() {}

    public static Transform getInstance() {
        return addArgAssignment;
    }

    /**
     * int a = constant; -> final int b = constant; int a = b;
     * Var A = NEW B(xxx, yyy); => n = xxx; Var A = NEW B(n, yyy);
     * Similary, ExpressionStatement: MethodInvocation, Assignment
     * Notice: New Assignment should be added final modifier
     */
    @Override
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brotherStatement, ASTNode sourceStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
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
                        if(value.startsWith("0x") || value.startsWith("0X")) {
                            value = value.substring(2);
                        }
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
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(node instanceof VariableDeclarationStatement) {
            VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) node).fragments().get(0);
            Expression rightExpression = vdFragment.getInitializer();
            if(rightExpression instanceof MethodInvocation || rightExpression instanceof ClassInstanceCreation) {
                nodes.add(node);
            }
        }
        if(node instanceof ExpressionStatement) {
            Expression expression = ((ExpressionStatement) node).getExpression();
            if(expression instanceof MethodInvocation || expression instanceof Assignment) {
                nodes.add(node);
            }
        }
        return nodes;
    }

}