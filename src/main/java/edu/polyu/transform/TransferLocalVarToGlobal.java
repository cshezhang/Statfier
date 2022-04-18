package edu.polyu.transform;


import edu.polyu.analysis.ASTWrapper;
import edu.polyu.util.Util;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Util.checkExpressionLiteral;
import static edu.polyu.util.Util.checkLiteralType;
import static edu.polyu.util.Util.getChildrenNodes;

public class TransferLocalVarToGlobal extends Transform {

    private static int varCounter;
    private static TransferLocalVarToGlobal instance = new TransferLocalVarToGlobal();

    private TransferLocalVarToGlobal() {
        varCounter = 0;
    }

    public static TransferLocalVarToGlobal getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brotherStatement, ASTNode sourceStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        Expression literalNode = (Expression) targetNode;
        TypeDeclaration clazz = Util.getClassOfStatement(sourceStatement);
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

    @Override
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode statement) {
        List<ASTNode> nodes = new ArrayList<>();
        List<ASTNode> subNodes = getChildrenNodes(statement);
        for(int i = 0; i < subNodes.size(); i++) {
            ASTNode subNode = subNodes.get(i);
            if(checkExpressionLiteral(subNode)) {
                nodes.add(subNode);
            }
        }
        return nodes;
    }

}
