package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import static edu.polyu.Util.getDirectBlockOfStatement;

public class CFWrapperWithIfFlase extends Mutator {

    private static final CFWrapperWithIfFlase instance = new CFWrapperWithIfFlase();

    private CFWrapperWithIfFlase() {}

    public static CFWrapperWithIfFlase getInstance() {
        return instance;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
        Block newIfBlock = ast.newBlock();
        newIfBlock.statements().add(newStatement);
        IfStatement newIfStatement = ast.newIfStatement();
        newIfStatement.setExpression(ast.newBooleanLiteral(false));
        newIfStatement.setThenStatement(newIfBlock);
        Block oldBlock = getDirectBlockOfStatement(sourceStatement);
        if(oldBlock.statements().contains(sourceStatement)) {
            ListRewrite listRewrite = astRewrite.getListRewrite(oldBlock, Block.STATEMENTS_PROPERTY);
            listRewrite.insertBefore(newIfStatement, sourceStatement, null);
        } else {
            Block newBlock = ast.newBlock();
            newBlock.statements().add(ASTNode.copySubtree(ast, sourceStatement));
            newBlock.statements().add(newIfStatement);
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
