package org.rainyday.transform;

import org.rainyday.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
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
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brother, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        ASTNode parNode = srcNode.getParent();
        if(parNode instanceof Block) {
            if(((Block) parNode).statements().size() == 1) {
                return false;
            }
        }
        IfStatement newIfStatement = ast.newIfStatement();
        Block block = ast.newBlock();
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, srcNode);
        block.statements().add(newStatement);
        newIfStatement.setExpression(ast.newBooleanLiteral(true));
        newIfStatement.setThenStatement(block);
        astRewrite.replace(srcNode, newIfStatement, null);
        wrapper.getPriorNodes().add(newIfStatement.getExpression());
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if (TypeWrapper.isLiteral(node)) {
            return nodes;
        }
        ASTNode par = node.getParent();
        if (node instanceof Statement && (par instanceof IfStatement || par instanceof WhileStatement ||
                par instanceof DoStatement || par instanceof ForStatement)) {
            return nodes;
        }
        if (node instanceof VariableDeclarationStatement || node instanceof FieldDeclaration ||
                node instanceof MethodDeclaration || node instanceof ReturnStatement || node instanceof SuperConstructorInvocation) {
            return nodes;
        }
        nodes.add(node);
        return nodes;
    }

}
