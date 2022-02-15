package edu.polyu.transform;



import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

// Control flow warpper by if(true)
public class CFWrapperWithIfTrue extends Transform {

    private static final CFWrapperWithIfTrue instance = new CFWrapperWithIfTrue();

    private CFWrapperWithIfTrue() {}

    public static CFWrapperWithIfTrue getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brother, ASTNode sourceStatement) {
        IfStatement ifStatement = ast.newIfStatement();
        Block block = ast.newBlock();
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
        block.statements().add(newStatement);
        ifStatement.setExpression(ast.newBooleanLiteral(true));
        ifStatement.setThenStatement(block);
        astRewrite.replace(sourceStatement, ifStatement, null);
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
