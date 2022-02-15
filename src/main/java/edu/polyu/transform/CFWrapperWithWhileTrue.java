package edu.polyu.transform;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * @Description: Control flow wrapper by while(true)-break
 * @Author: Vanguard
 * @Date: 2021-10-20 18:17
 */
public class CFWrapperWithWhileTrue extends Transform {

    private static final CFWrapperWithWhileTrue instance = new CFWrapperWithWhileTrue();

    private CFWrapperWithWhileTrue() {}

    public static CFWrapperWithWhileTrue getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brother, ASTNode sourceStatement) {
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
        WhileStatement whileStatement = ast.newWhileStatement();
        whileStatement.setExpression(ast.newBooleanLiteral(true));
        BreakStatement breakStatement = ast.newBreakStatement();
        Block block = ast.newBlock();
        block.statements().add(newStatement);
        block.statements().add(breakStatement);
        whileStatement.setBody(block);
        astRewrite.replace(sourceStatement, whileStatement, null);
        return true;
    }

    @Override
    public int check(ASTNode node) {
        if(node instanceof VariableDeclarationStatement || node instanceof FieldDeclaration || node instanceof MethodDeclaration) {
            return 0;
        }
        return 1;
    }

}
