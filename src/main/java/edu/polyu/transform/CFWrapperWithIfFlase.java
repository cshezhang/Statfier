package edu.polyu.transform;


import edu.polyu.analysis.ASTWrapper;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Util.checkExpressionLiteral;
import static edu.polyu.util.Util.getDirectBlockOfStatement;
import static edu.polyu.util.Util.spotBugsJarList;

public class CFWrapperWithIfFlase extends Transform {

    private static final CFWrapperWithIfFlase instance = new CFWrapperWithIfFlase();

    private CFWrapperWithIfFlase() {}

    public static CFWrapperWithIfFlase getInstance() {
        return instance;
    }

    /*
    S; -> if(false) S; S;
     */
    @Override
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brother, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, srcNode);
        Block newIfBlock = ast.newBlock();
        newIfBlock.statements().add(newStatement);
        IfStatement newIfStatement = ast.newIfStatement();
        newIfStatement.setExpression(ast.newBooleanLiteral(false));
        newIfStatement.setThenStatement(newIfBlock);
        Block oldBlock = getDirectBlockOfStatement(srcNode);
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
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode srcNode) {
        List<ASTNode> nodes = new ArrayList<>();
        if (checkExpressionLiteral(srcNode) || !(srcNode instanceof Statement)) {
            return nodes;
        }
        if (srcNode instanceof VariableDeclarationStatement || srcNode instanceof FieldDeclaration ||
                srcNode instanceof MethodDeclaration || srcNode instanceof ReturnStatement || srcNode instanceof SuperConstructorInvocation) {
            return nodes;
        }
        nodes.add(srcNode);
        return nodes;
    }

}
