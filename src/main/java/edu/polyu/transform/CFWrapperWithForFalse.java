package edu.polyu.transform;


import edu.polyu.analysis.ASTWrapper;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Util.getDirectBlockOfStatement;

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
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brother, ASTNode sourceStatement) {
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

        Block newForBody = ast.newBlock();
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
        newForBody.statements().add(newStatement);
        newForStatement.setBody(newForBody);
        Block methodBlock = getDirectBlockOfStatement(sourceStatement);
        if(methodBlock.statements().contains(sourceStatement)) {
            ListRewrite methodRewrite = astRewrite.getListRewrite(methodBlock, Block.STATEMENTS_PROPERTY);
            methodRewrite.insertAfter(newForStatement, sourceStatement, null);
        } else {
            Block newBlock = ast.newBlock();
            newBlock.statements().add(ASTNode.copySubtree(ast, sourceStatement));
            newBlock.statements().add(newForStatement);
            astRewrite.replace(sourceStatement, newBlock, null);
        }
        return true;
    }

    @Override
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(node instanceof VariableDeclarationStatement || node instanceof FieldDeclaration ||
                node instanceof MethodDeclaration || node instanceof ReturnStatement || node instanceof SuperConstructorInvocation) {
            return nodes;
        }
        nodes.add(node);
        return nodes;
    }

}
