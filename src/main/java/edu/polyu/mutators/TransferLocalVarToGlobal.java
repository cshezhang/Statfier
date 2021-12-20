/*
 * @Description: 
 * @Author: Austin ZHANG
 * @Date: 2021-10-15 16:42:52
 */
package edu.polyu.mutators;

import edu.polyu.Mutator;
import edu.polyu.Util;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.HashSet;
import java.util.List;

/**
 * Description:
 * Author: Austin Zhang
 * Date: 2021/8/24 9:05 下午
 */

public class TransferLocalVarToGlobal extends Mutator {

    private static TransferLocalVarToGlobal transferLocalVarToGlobal = new TransferLocalVarToGlobal();

    private TransferLocalVarToGlobal() {}

    public static TransferLocalVarToGlobal getInstance() {
        return transferLocalVarToGlobal;
    }

    @Override
    public int getIndex() {
        return 6;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        TypeDeclaration sourceType = Util.getTypeOfStatement(sourceStatement);
        List<ASTNode> classComponents = (List<ASTNode>) sourceType.getStructuralProperty(sourceType.getBodyDeclarationsProperty());
        HashSet<String> globalNames = new HashSet<>();
        for(ASTNode node : classComponents) {
            if(node instanceof FieldDeclaration) {
                FieldDeclaration fieldDeclaration = (FieldDeclaration) node;
                VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) fieldDeclaration.fragments().get(0);
                globalNames.add(vdFragment.getName().toString());
            }
        }
        VariableDeclarationStatement oldVdStatement = (VariableDeclarationStatement) sourceStatement;
        String varName = ((VariableDeclarationFragment) oldVdStatement.fragments().get(0)).getName().toString();
        if(globalNames.contains(varName)) {
            return false;
        }
        VariableDeclarationStatement newVdStatement = (VariableDeclarationStatement) ASTNode.copySubtree(ast, oldVdStatement);
        newVdStatement.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
        ListRewrite classRewrite = astRewrite.getListRewrite(sourceType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        classRewrite.insertFirst(newVdStatement, null);
        astRewrite.remove(oldVdStatement, null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            Expression rightValue = ((VariableDeclarationFragment)((VariableDeclarationStatement) statement).fragments().get(0)).getInitializer();
            if(rightValue != null && Util.checkExpressionLiteral(rightValue)) {
                return true;
            }
        }
        return false;
    }
}

