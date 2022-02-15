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
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import static edu.polyu.util.Util.getDirectBlockOfStatement;

public class CFWrapperWithIfFlase extends Transform {

    private static final CFWrapperWithIfFlase instance = new CFWrapperWithIfFlase();

    private CFWrapperWithIfFlase() {}

    public static CFWrapperWithIfFlase getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brother, ASTNode oldStatement) {
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, oldStatement);
        Block newIfBlock = ast.newBlock();
        newIfBlock.statements().add(newStatement);
        IfStatement newIfStatement = ast.newIfStatement();
        newIfStatement.setExpression(ast.newBooleanLiteral(false));
        newIfStatement.setThenStatement(newIfBlock);
        Block oldBlock = getDirectBlockOfStatement(oldStatement);
        if(oldBlock.statements().contains(oldStatement)) {
            ListRewrite listRewrite = astRewrite.getListRewrite(oldBlock, Block.STATEMENTS_PROPERTY);
            listRewrite.insertBefore(newIfStatement, oldStatement, null);
        } else {
            Block newBlock = ast.newBlock();
            newBlock.statements().add(ASTNode.copySubtree(ast, oldStatement));
            newBlock.statements().add(newIfStatement);
            astRewrite.replace(oldStatement, newBlock, null);
        }
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
