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

import static edu.polyu.Util.checkLiteralType;
import static edu.polyu.Util.getChildrenNodes;

/**
 * @Description: transfer local variable declaration to global scope in class, and this mutator contains static modifier
 * @Author: Vanguard
 * @Date: 2021-10-05 12:49
 */
public class TransferLocalVarToStaticGlobal extends Mutator {

    private static int varCounter;
    private static TransferLocalVarToStaticGlobal instance = new TransferLocalVarToStaticGlobal();

    private TransferLocalVarToStaticGlobal() {
        varCounter = 0;
    }

    public static TransferLocalVarToStaticGlobal getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        List<ASTNode> subNodes = getChildrenNodes(sourceStatement);
        List<ASTNode> literalNodes = new ArrayList<>();
        for(int i = 0; i < subNodes.size(); i++) {
            ASTNode node = subNodes.get(i);
            if(Util.checkExpressionLiteral(node)) {
                literalNodes.add(node);
            }
        }
        Expression targetLiteral = (Expression) literalNodes.get(index);
        TypeDeclaration clazz = Util.getTypeOfStatement(sourceStatement);
        String newVarName = "t2sg" + varCounter++;
        SimpleName newVar = ast.newSimpleName(newVarName);
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        newVdFragment.setName(newVar);
        newVdFragment.setInitializer((Expression) ASTNode.copySubtree(ast, targetLiteral));
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(newVdFragment);
        fieldDeclaration.setType(checkLiteralType(ast, targetLiteral));
        fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
        fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertFirst(fieldDeclaration, null);
        astRewrite.replace(targetLiteral, newVar, null);
        return true;
    }

    @Override
    public int check(Statement statement) {
        int counter = 0;
        List<ASTNode> nodes = getChildrenNodes(statement);
        for(int i = 0; i < nodes.size(); i++) {
            if(Util.checkExpressionLiteral(nodes.get(i))) {
                counter++;
            }
        }
        return counter;
    }

}
