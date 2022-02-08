package edu.polyu.mutators;


import edu.polyu.Util;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.Util.checkExpressionLiteral;
import static edu.polyu.Util.checkLiteralType;
import static edu.polyu.Util.getChildrenNodes;

public class TransferLocalVarToGlobal extends Mutator {

    private static int varCounter;
    private static TransferLocalVarToGlobal instance = new TransferLocalVarToGlobal();

    private TransferLocalVarToGlobal() {
        varCounter = 0;
    }

    public static TransferLocalVarToGlobal getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        List<ASTNode> subNodes = getChildrenNodes(sourceStatement);
        List<ASTNode> literalNodes = new ArrayList<>();
        for(int i = 0; i < subNodes.size(); i++) {
            ASTNode node = subNodes.get(i);
            if(checkExpressionLiteral(node)) {
                literalNodes.add(node);
            }
        }
        Expression literalNode = (Expression) literalNodes.get(index);
        TypeDeclaration clazz = Util.getTypeOfStatement(sourceStatement);
        String newVarName = "t2g" + varCounter++;
        SimpleName newVar = ast.newSimpleName(newVarName);
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        newVdFragment.setName(newVar);
        newVdFragment.setInitializer((Expression) ASTNode.copySubtree(ast, literalNode));
        FieldDeclaration newFieldDeclaration = ast.newFieldDeclaration(newVdFragment);
        newFieldDeclaration.setType(checkLiteralType(ast, literalNode));
        newFieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertFirst(newFieldDeclaration, null);
        astRewrite.replace(literalNode, newVar, null);
        return true;
    }

//    @Override
//    public List<ASTNode> getCandidateNodes(Statement statement) {
//        List<ASTNode> nodes = getChildrenNodes(statement);
//        List<ASTNode> results = new ArrayList<>();
//        for(int i = 0; i < nodes.size(); i++) {
//            if((checkExpressionLiteral(nodes.get(i)))) {
//                results.add(nodes.get(i));
//            }
//        }
//        return results;
//    }

    @Override
    public int check(Statement statement) {
        int counter = 0;
        List<ASTNode> nodes = getChildrenNodes(statement);
        for(int i = 0; i < nodes.size(); i++) {
            if(checkExpressionLiteral(nodes.get(i))) {
                counter++;
            }
        }
        return counter;
    }

}
