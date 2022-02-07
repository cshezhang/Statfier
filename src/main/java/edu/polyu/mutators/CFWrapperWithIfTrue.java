package edu.polyu.mutators;


<<<<<<< HEAD
import edu.polyu.Mutator;
=======
>>>>>>> d2a3d5d792e1b70378656198bac2f3a1c133ad84
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

// Control flow warpper by if(true)
public class CFWrapperWithIfTrue extends Mutator {

    private static final CFWrapperWithIfTrue instance = new CFWrapperWithIfTrue();

    private CFWrapperWithIfTrue() {}

    public static CFWrapperWithIfTrue getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        IfStatement ifStatement = ast.newIfStatement();
        Block block = ast.newBlock();
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
        block.statements().add(newStatement);
        ifStatement.setExpression(ast.newBooleanLiteral(true));
        ifStatement.setThenStatement(block);
        astRewrite.replace(sourceStatement, ifStatement, null);
        return true;
    }

//    @Override
//    public List<ASTNode> getCandidateNodes(Statement statement) {
//        List<ASTNode> candidateNodes = new ArrayList<>();
//        if(!(statement instanceof VariableDeclarationStatement)) {
//            candidateNodes.add(statement);
//        }
//        return candidateNodes;
//    }

    @Override
    public int check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            return 0;
        }
        return 1;
    }

}
