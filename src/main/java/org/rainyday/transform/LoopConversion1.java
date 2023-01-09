package org.rainyday.transform;

import org.rainyday.analysis.TypeWrapper;
import org.rainyday.analysis.LoopStatement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Transfer a Loop to Lambda Expression Loop
 * @Author: Vanguard
 * @Date: 2022-02-26 15:42
 */
public class LoopConversion1 extends Transform {

    private static LoopConversion1 loopConversion1 = new LoopConversion1();

    private LoopConversion1() {}

    public static LoopConversion1 getInstance() {
        return loopConversion1;
    }


    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode broNode, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        LoopStatement loopStatement = new LoopStatement(targetNode);
        if (!loopStatement.isReachable()) {
            return false;
        }
        MethodInvocation newExpression = ast.newMethodInvocation();
        if (loopStatement.isEnhancedForStatement()) {
            if(!(loopStatement.getExpression() instanceof SimpleName)) {
                return false;
            }
            SimpleName varName = ast.newSimpleName(((SimpleName) loopStatement.getExpression()).getIdentifier());
            newExpression.setExpression(varName);
            newExpression.setName(ast.newSimpleName("forEach"));
            LambdaExpression lambdaExpression = ast.newLambdaExpression();
            VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
            SingleVariableDeclaration var = ((EnhancedForStatement) loopStatement.getLoopStatement()).getParameter();
            fragment.setName(ast.newSimpleName(var.getName().getIdentifier()));
            lambdaExpression.parameters().add(fragment);
            ASTNode newBody = ASTNode.copySubtree(ast, ((EnhancedForStatement) loopStatement.getLoopStatement()).getBody());
            if(newBody instanceof Block || newBody instanceof Expression) {
                lambdaExpression.setBody(newBody);
            } else {
                return false;
            }
            newExpression.arguments().add(lambdaExpression);
            ExpressionStatement newStatement = ast.newExpressionStatement(newExpression);
            astRewrite.replace(targetNode, newStatement, null);
            return true;
        }
        if (loopStatement.isForStatement()) {
            ForStatement forStatement = (ForStatement) targetNode;
            Statement initStatement;  // Integer a = list.get(i);
            if (forStatement.getBody() instanceof Block) {
                if(forStatement.getBody() == null || ((Block)forStatement.getBody()).statements().size() == 0) {
                    return false;
                }
                initStatement = (Statement) ((Block) forStatement.getBody()).statements().get(0);
            } else {
                return false;
            }
            if(forStatement.getExpression() == null) {
                return false;
            }
            Expression expression = forStatement.getExpression();  // i < list.size()
            if (initStatement instanceof VariableDeclarationStatement && expression.toString().contains(".size()") && expression instanceof InfixExpression) {
                Expression rightOperand = ((InfixExpression) expression).getRightOperand();
                VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) initStatement).fragments().get(0);
                Expression initializer = vdFragment.getInitializer();
                if (rightOperand instanceof MethodInvocation) {  // list.get(i)
                    if (initializer instanceof MethodInvocation) {
                        if (((MethodInvocation) rightOperand).getExpression() instanceof SimpleName && ((MethodInvocation) initializer).getExpression() instanceof SimpleName) {
                            String sizeInvoker = ((SimpleName) ((MethodInvocation) rightOperand).getExpression()).getIdentifier(); // list in list.size()
                            String getInvoker = ((SimpleName) ((MethodInvocation) initializer).getExpression()).getIdentifier(); // list
                            String get = ((MethodInvocation) initializer).getName().getIdentifier(); // get
                            if (getInvoker.equals(sizeInvoker) && "get".equals(get)) {
                                String varName = vdFragment.getName().getIdentifier(); // a from Integer a = list.get(i);
                                newExpression.setName(ast.newSimpleName(getInvoker)); // item in list.forEach(item)
                                newExpression.setName(ast.newSimpleName("forEach"));
                                LambdaExpression lambdaExpression = ast.newLambdaExpression();
                                VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
                                fragment.setName(ast.newSimpleName(varName)); // list in list.forEach(item)
                                lambdaExpression.parameters().add(fragment);
                                Block newBlock = (Block) ASTNode.copySubtree(ast, forStatement.getBody());
                                newBlock.statements().remove(0);
                                lambdaExpression.setBody(newBlock);
                                ExpressionStatement newStatement = ast.newExpressionStatement(newExpression);
                                astRewrite.replace(targetNode, newStatement, null);
                                return true;
                            }
                        }
                    } else {
                        if (((MethodInvocation) rightOperand).getExpression() instanceof SimpleName) {
                            String varName = vdFragment.getName().getIdentifier(); // a from Integer a = list.get(i);
                            if(varName.endsWith("s")) {
                                varName = varName.substring(0, varName.length() - 1);
                            } else {
                                varName = "forEachVar";
                            }
                            String sizeInvoker = ((SimpleName) ((MethodInvocation) rightOperand).getExpression()).getIdentifier();
                            newExpression.setName(ast.newSimpleName("forEach"));
                            newExpression.setExpression(ast.newSimpleName(sizeInvoker)); // item in list.forEach(item)
                            LambdaExpression lambdaExpression = ast.newLambdaExpression();
                            VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
                            fragment.setName(ast.newSimpleName(varName)); // list in list.forEach(item)
                            lambdaExpression.parameters().add(fragment);
                            Block newBlock = (Block) ASTNode.copySubtree(ast, forStatement.getBody());
                            lambdaExpression.setBody(newBlock);
                            newExpression.arguments().add(lambdaExpression);
                            ExpressionStatement newStatement = ast.newExpressionStatement(newExpression);
                            astRewrite.replace(targetNode, newStatement, null);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        while(true) {
            if(node == null || node instanceof FieldDeclaration || node instanceof MethodDeclaration || node instanceof TypeDeclaration) {
                break;
            }
            if (LoopStatement.isLoopStatement(node)) {
                nodes.add(node);
            }
            node = node.getParent();
        }
        return nodes;
    }

    @Override
    public String getIndex() {
        return "LoopConversion1";
    }

}
