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

// Control flow wrapper based on for-loop
public class CFWrapperWithForTrue extends Mutator {

    private static final CFWrapperWithForTrue instance = new CFWrapperWithForTrue();

    private static int varCounter;

    private CFWrapperWithForTrue() {
        varCounter = 0;
    }

    public static CFWrapperWithForTrue getInstance() {
        return instance;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        ForStatement newForStatement = ast.newForStatement();

        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        String controlVar = String.format("cfwwft%d", varCounter++);
        newVdFragment.setName(ast.newSimpleName(controlVar));
        newVdFragment.setInitializer(ast.newNumberLiteral("0"));

        VariableDeclarationExpression newVdExpression = ast.newVariableDeclarationExpression(newVdFragment);
        newVdExpression.setType(ast.newPrimitiveType(PrimitiveType.INT));
        newForStatement.initializers().add(newVdExpression);

        InfixExpression infixExpression = ast.newInfixExpression();
        infixExpression.setLeftOperand(ast.newSimpleName(controlVar));
        infixExpression.setOperator(InfixExpression.Operator.LESS);
        infixExpression.setRightOperand(ast.newNumberLiteral("1"));
        newForStatement.setExpression(infixExpression);

        PostfixExpression postfixExpression = ast.newPostfixExpression();
        postfixExpression.setOperand(ast.newSimpleName(controlVar));
        postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
        newForStatement.updaters().add(postfixExpression);

        Block newForBodyBlock = ast.newBlock();
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
        newForBodyBlock.statements().add(newStatement);
        newForStatement.setBody(newForBodyBlock);
        astRewrite.replace(sourceStatement, newForStatement, null);
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
