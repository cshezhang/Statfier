package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.Util.*;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021-12-29 16:10
 */
public class AddMethodCallToLiteral extends Mutator {

    private static int literalCounter = 0;

    private static final AddMethodCallToLiteral addMethodCallToLiteral = new AddMethodCallToLiteral();
    private AddMethodCallToLiteral() {}

    public static AddMethodCallToLiteral getInstance() {
        return addMethodCallToLiteral;
    }

    @Override
    public boolean transform(int index, AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        List<ASTNode> nodes = getChildrenNodes(sourceStatement);
        List<ASTNode> literalNodes = new ArrayList<>();
        for(ASTNode node : nodes) {
            if(checkExpressionLiteral(node)) {
                literalNodes.add(node);
            }
        }
        if(literalNodes.size() == 0) {
            return false;
        }
        ASTNode targetNode = literalNodes.get(index);
        MethodDeclaration newMethod = ast.newMethodDeclaration();
        String newMethodName = "getLiteral" + literalCounter++;
        newMethod.setName(ast.newSimpleName(newMethodName));
        ReturnStatement returnStatement = ast.newReturnStatement();
        Block newBlock = ast.newBlock();
        newBlock.statements().add(returnStatement);
        newMethod.setBody(newBlock);
        returnStatement.setExpression((Expression) ASTNode.copySubtree(ast, targetNode));
        MethodDeclaration oldMethod = getDirectMethodOfStatement(sourceStatement);
        TypeDeclaration clazz = (TypeDeclaration) oldMethod.getParent();
        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertFirst(newMethod, null);
        MethodInvocation newMethodInvocation = ast.newMethodInvocation();
        newMethodInvocation.setName(ast.newSimpleName(newMethodName));
        astRewrite.replace(targetNode, newMethodInvocation, null);
        return true;
    }

    @Override
    public int check(Statement statement) {
        int counter = 0;
        List<ASTNode> nodes = getChildrenNodes(statement);
        for(ASTNode astNode : nodes) {
            if(checkExpressionLiteral(astNode)) {
                counter++;
            }
        }
        return counter;
    }

}
