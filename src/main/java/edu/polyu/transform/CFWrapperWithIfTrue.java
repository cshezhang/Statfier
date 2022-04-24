package edu.polyu.transform;

import edu.polyu.analysis.ASTWrapper;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

// Control flow warpper by if(true)
public class CFWrapperWithIfTrue extends Transform {

    private static final CFWrapperWithIfTrue instance = new CFWrapperWithIfTrue();

    private CFWrapperWithIfTrue() {}

    public static CFWrapperWithIfTrue getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brother, ASTNode sourceStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
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
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(node instanceof VariableDeclarationStatement || node instanceof FieldDeclaration ||
                node instanceof MethodDeclaration || node instanceof ReturnStatement || node instanceof SuperConstructorInvocation) {
            return nodes;
        }
        nodes.add(node);
        return nodes;
    }

}
