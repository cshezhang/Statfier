package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.Util.*;

public class AddMethodCall extends Mutator {

    private static int literalCounter = 0;

    private static AddMethodCall addMethodCall = new AddMethodCall();
    private AddMethodCall() {}

    public static AddMethodCall getInstance() {
        return addMethodCall;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
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
        int randomIndex = random.nextInt(literalNodes.size());
        ASTNode targetNode = literalNodes.get(randomIndex);
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
        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, clazz.getBodyDeclarationsProperty());
        listRewrite.insertFirst(newMethod, null);
        MethodInvocation methodInvocation = ast.newMethodInvocation();
        methodInvocation.setName(ast.newSimpleName(newMethodName));
        astRewrite.replace(targetNode, methodInvocation, null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        List<ASTNode> nodes = getChildrenNodes(statement);
        for(ASTNode astNode : nodes) {
            if(checkExpressionLiteral(astNode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getIndex() {
        return 5;
    }
}
