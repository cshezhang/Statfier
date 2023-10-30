package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

public class CFWrapperWithIfFalse extends Transform {

    private static final CFWrapperWithIfFalse instance = new CFWrapperWithIfFalse();

    private CFWrapperWithIfFalse() {}

    public static CFWrapperWithIfFalse getInstance() {
        return instance;
    }

    /*
    S; -> if(false) S; S;
     */
    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brother, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, srcNode);
        Block newIfBlock = ast.newBlock();
        newIfBlock.statements().add(newStatement);
        IfStatement newIfStatement = ast.newIfStatement();
        newIfStatement.setExpression(ast.newBooleanLiteral(false));
        newIfStatement.setThenStatement(newIfBlock);
        Block oldBlock = TypeWrapper.getDirectBlockOfStatement(srcNode);
        if(oldBlock.statements().contains(srcNode)) {
            ListRewrite listRewrite = astRewrite.getListRewrite(oldBlock, Block.STATEMENTS_PROPERTY);
            listRewrite.insertBefore(newIfStatement, srcNode, null);
        } else {
            Block newBlock = ast.newBlock();
            newBlock.statements().add(newIfStatement);
            newBlock.statements().add(ASTNode.copySubtree(ast, srcNode));
            astRewrite.replace(srcNode, newBlock, null);
        }
        wrapper.getPriorNodes().add(newIfStatement);
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if (TypeWrapper.isLiteral(node)) {
            return nodes;
        }
        if(!(node instanceof Statement) && !(node instanceof Block)) {
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
