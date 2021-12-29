package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

public class CFWrapperWithWhileFalse extends Mutator {

    private static final CFWrapperWithWhileFalse instance = new CFWrapperWithWhileFalse();

    private CFWrapperWithWhileFalse() {}

    public static CFWrapperWithWhileFalse getInstance() {
        return instance;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
        WhileStatement newWhileStatement = ast.newWhileStatement();
        newWhileStatement.setExpression(ast.newBooleanLiteral(false));
        BreakStatement breakStatement = ast.newBreakStatement();
        Block newWhileBlock = ast.newBlock();
        newWhileBlock.statements().add(newStatement);
        newWhileBlock.statements().add(breakStatement);
        newWhileStatement.setBody(newWhileBlock);
        MethodDeclaration method = ast.newMethodDeclaration();
        ListRewrite listRewrite = astRewrite.getListRewrite(method.getBody(), Block.STATEMENTS_PROPERTY);
        listRewrite.insertAfter(newWhileStatement, sourceStatement, null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            return false;
        }
        return true;
    }

    @Override
    public int getIndex() {
        return 0;
    }

}
