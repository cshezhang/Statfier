package org.detector.transform;

import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.detector.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

public class CFWrapperWithForFalse extends Transform {

    private static final CFWrapperWithForFalse instance = new CFWrapperWithForFalse();

    private static int varCounter;

    private CFWrapperWithForFalse() {
        varCounter = 0;
    }

    public static CFWrapperWithForFalse getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brother, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        ForStatement newForStatement = ast.newForStatement();
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        String newControlVar = String.format("cfwwff%d", varCounter++);
        newVdFragment.setName(ast.newSimpleName(newControlVar));
        newVdFragment.setInitializer(ast.newNumberLiteral("0"));
        VariableDeclarationExpression newVdExpression = ast.newVariableDeclarationExpression(newVdFragment);
        newVdExpression.setType(ast.newPrimitiveType(PrimitiveType.INT));
        newForStatement.initializers().add(newVdExpression);

        InfixExpression infixExpression = ast.newInfixExpression();
        infixExpression.setLeftOperand(ast.newSimpleName(newControlVar));
        infixExpression.setOperator(InfixExpression.Operator.LESS);
        infixExpression.setRightOperand(ast.newNumberLiteral("0"));
        newForStatement.setExpression(infixExpression);

        PostfixExpression postfixExpression = ast.newPostfixExpression();
        postfixExpression.setOperand(ast.newSimpleName(newControlVar));
        postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
        newForStatement.updaters().add(postfixExpression);

        Block newBodyBlock = ast.newBlock();
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, srcNode);
        newBodyBlock.statements().add(newStatement);
        newForStatement.setBody(newBodyBlock);
        Block methodBlock = TypeWrapper.getDirectBlockOfStatement(srcNode);
        if(methodBlock.statements().contains(srcNode)) {
            ListRewrite methodRewrite = astRewrite.getListRewrite(methodBlock, Block.STATEMENTS_PROPERTY);
            methodRewrite.insertBefore(newForStatement, srcNode, null);
        } else {
            Block newBlock = ast.newBlock();
            newBlock.statements().add(newForStatement);
            newBlock.statements().add(ASTNode.copySubtree(ast, srcNode));
            astRewrite.replace(srcNode, newBlock, null);
        }
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(!(node instanceof Statement) && !(node instanceof Block)) {
            return nodes;
        }
        if(node instanceof VariableDeclarationStatement || node instanceof FieldDeclaration || node instanceof EmptyStatement ||
                node instanceof MethodDeclaration || node instanceof ReturnStatement || node instanceof SuperConstructorInvocation) {
            return nodes;
        }
        if(node.getParent().getParent() instanceof MethodDeclaration) {
            MethodDeclaration method = (MethodDeclaration) node.getParent().getParent();
            List<ASTNode> statements = method.getBody().statements();
            if(method.isConstructor() && !statements.isEmpty() && node == statements.get(0) && node instanceof ConstructorInvocation) {
                return nodes;
            }
        }
        nodes.add(node);
        return nodes;
    }

}
