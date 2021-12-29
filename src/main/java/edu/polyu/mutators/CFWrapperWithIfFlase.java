package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import static edu.polyu.Util.getDirectMethodOfStatement;

public class CFWrapperWithIfFlase extends Mutator {

    private static final CFWrapperWithIfFlase instance = new CFWrapperWithIfFlase();

    private CFWrapperWithIfFlase() {}

    public static CFWrapperWithIfFlase getInstance() {
        return instance;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
        IfStatement ifStatement = ast.newIfStatement();
        ifStatement.setExpression(ast.newBooleanLiteral(false));
        ifStatement.setThenStatement(newStatement);
        ListRewrite listRewrite = astRewrite.getListRewrite(sourceStatement.getParent(), Block.STATEMENTS_PROPERTY);
        listRewrite.insertBefore(ifStatement, sourceStatement, null);
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
