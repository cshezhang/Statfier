package edu.polyu.mutators;

<<<<<<< HEAD
import edu.polyu.Mutator;
=======
>>>>>>> d2a3d5d792e1b70378656198bac2f3a1c133ad84
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

// Control flow wrapper by while(true)-break
public class CFWrapperWithWhileTrue extends Mutator {

    private static final CFWrapperWithWhileTrue instance = new CFWrapperWithWhileTrue();

    private CFWrapperWithWhileTrue() {}

    public static CFWrapperWithWhileTrue getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
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
    public int check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            return 0;
        }
        return 1;
    }

}
