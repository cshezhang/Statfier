package edu.polyu.transform;

import edu.polyu.analysis.ASTWrapper;
import edu.polyu.util.Util;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

// Control flow wrapper based on do-while(false)
public class CFWrapperWithDoWhile extends Transform {

    private static final CFWrapperWithDoWhile instance = new CFWrapperWithDoWhile();

    private CFWrapperWithDoWhile() {}

    public static CFWrapperWithDoWhile getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brother, ASTNode sourceStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
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
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(Util.checkExpressionLiteral(node)) {
            return nodes;
        }
        if(node instanceof VariableDeclarationStatement || node instanceof FieldDeclaration ||
                node instanceof MethodDeclaration || node instanceof ReturnStatement || node instanceof SuperConstructorInvocation) {
            return nodes;
        }
        nodes.add(node);
        return nodes;
    }

}
