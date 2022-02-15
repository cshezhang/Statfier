package edu.polyu.transform;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

// Control flow wrapper based on do-while(false)
public class CFWrapperWithDoWhile extends Transform {

    private static final CFWrapperWithDoWhile instance = new CFWrapperWithDoWhile();

    private CFWrapperWithDoWhile() {}

    public static CFWrapperWithDoWhile getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brother, ASTNode sourceStatement) {
        DoStatement newDoStatement = ast.newDoStatement();
        newDoStatement.setExpression(ast.newBooleanLiteral(false));
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
        Block newMethodBlock = ast.newBlock();
        newMethodBlock.statements().add(newStatement);
        newDoStatement.setBody(newMethodBlock);
        astRewrite.replace(sourceStatement, newDoStatement, null);
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
