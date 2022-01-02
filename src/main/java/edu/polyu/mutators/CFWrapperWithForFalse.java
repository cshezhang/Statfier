package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import static edu.polyu.Util.getDirectBlockOfStatement;

public class CFWrapperWithForFalse extends Mutator {

    private static final CFWrapperWithForFalse instance = new CFWrapperWithForFalse();

    private static int varCounter;

    private CFWrapperWithForFalse() {
        varCounter = 0;
    }

    public static CFWrapperWithForFalse getInstance() {
        return instance;
    }

    @Override
    public boolean transform(int index, AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
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
    public int check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            return 0;
        }
        return 1;
    }

}
