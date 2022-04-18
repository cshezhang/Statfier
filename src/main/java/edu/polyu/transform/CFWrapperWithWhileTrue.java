package edu.polyu.transform;

import edu.polyu.analysis.ASTWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Control flow wrapper by while(true)-break
 * @Author: Vanguard
 * @Date: 2021-10-20 18:17
 */
public class CFWrapperWithWhileTrue extends Transform {

    private static final CFWrapperWithWhileTrue instance = new CFWrapperWithWhileTrue();

    private CFWrapperWithWhileTrue() {}

    public static CFWrapperWithWhileTrue getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brother, ASTNode sourceStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
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
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(node instanceof VariableDeclarationStatement || node instanceof FieldDeclaration ||
                node instanceof MethodDeclaration || node instanceof ReturnStatement) {
            return nodes;
        }
        nodes.add(node);
        return nodes;
    }

}
