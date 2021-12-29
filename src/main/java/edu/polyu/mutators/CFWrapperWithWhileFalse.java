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

import static edu.polyu.Util.getDirectBlockOfStatement;
import static edu.polyu.Util.getDirectMethodOfStatement;

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
        Block newWhileBlock = ast.newBlock();
        newWhileBlock.statements().add(newStatement);
        newWhileStatement.setBody(newWhileBlock);
        Block block = getDirectBlockOfStatement(sourceStatement);
        ListRewrite listRewrite = astRewrite.getListRewrite(block, Block.STATEMENTS_PROPERTY);
        try {
            listRewrite.insertAfter(newWhileStatement, sourceStatement, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            return false;
        }
        return true;
    }

}
