package edu.polyu.mutators;

import edu.polyu.Util;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: static int a = b; -> static int a; static {a = b;}
 * @Author: Vanguard
 * @Date: 2021-12-15 12:49
 */
public class AddGlobalAssignment extends Mutator {

    private static final AddGlobalAssignment instance = new AddGlobalAssignment();
    private AddGlobalAssignment() {}

    public static AddGlobalAssignment getInstance() {
        return instance;
    }

    /*
     * Attention: For FieldDeclaration
     * Source: Var A = B; => Var A; A = B; VD_Statement => VD + Assignment
     * Including final Var A = B; Attention: Final variable assignment cannot be applied to Array Type.
     * Beside, static variable can inited in static code block.
     * */
    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        TypeDeclaration oldClazz = Util.getTypeOfStatement(sourceStatement);
        List<ASTNode> oldClazzCompoents = oldClazz.bodyDeclarations();
        List<FieldDeclaration> oldFieldDeclarations = new ArrayList<>();
        for(int i = 0; i < oldClazzCompoents.size(); i++) {
            ASTNode node = oldClazzCompoents.get(i);
            if(node instanceof FieldDeclaration) {
                FieldDeclaration fieldDeclaration = (FieldDeclaration) node;
                List<ASTNode> modifiers = fieldDeclaration.modifiers();
                if(modifiers.contains(Modifier.ModifierKeyword.STATIC_KEYWORD)) {
                    VariableDeclarationFragment fragment = (VariableDeclarationFragment) fieldDeclaration.fragments().get(0);
                    // Search Field Declaration with Initializer
                    if (fragment.getInitializer() != null) {
                        oldFieldDeclarations.add(fieldDeclaration);
                    }
                }
            }
        }
        if(oldFieldDeclarations.size() == 0) {
            return false;
        }
        List<FieldDeclaration> targetFields = new ArrayList<>();
        List<ASTNode> children = Util.getChildrenNodes(sourceStatement);
        for(ASTNode node : children) {
            if(node instanceof SimpleName) {
                for(FieldDeclaration oldFieldDeclaration : oldFieldDeclarations) {
                    VariableDeclarationFragment fragment = (VariableDeclarationFragment) oldFieldDeclaration.fragments().get(0);
                    if(fragment.getName().getIdentifier().equals(((SimpleName) node).getIdentifier())) {
                        // Search matched filed: SimpleName in Statement -> FieldDeclaration
                        targetFields.add(oldFieldDeclaration);
                    }
                }
            }
        }
        if(targetFields.size() == 0) {
            return false;
        }
        Block newStaticBlock = ast.newBlock();
        for(int i = 0; i < targetFields.size(); i++) {
            FieldDeclaration oldFieldDeclaration = targetFields.get(i);
            VariableDeclarationFragment oldFragment = (VariableDeclarationFragment) oldFieldDeclaration.fragments().get(0);
            String varName = oldFragment.getName().getIdentifier();
            VariableDeclarationFragment newFragmentToField = ast.newVariableDeclarationFragment();
            newFragmentToField.setExtraDimensions(oldFragment.getExtraDimensions());
            newFragmentToField.setName(ast.newSimpleName(varName));
            FieldDeclaration newFieldDeclaration = ast.newFieldDeclaration(newFragmentToField);
            newFieldDeclaration.setType((Type) ASTNode.copySubtree(ast, oldFieldDeclaration.getType()));
            VariableDeclarationFragment newFragment2 = ast.newVariableDeclarationFragment();
            newFragment2.setName(ast.newSimpleName(varName));
            newFragment2.setExtraDimensions(oldFragment.getExtraDimensions());
            VariableDeclarationStatement newVdStatement = ast.newVariableDeclarationStatement(newFragment2);
            astRewrite.replace(oldFieldDeclaration, newFieldDeclaration, null);
            newStaticBlock.statements().add(newVdStatement);
        }
        ListRewrite listRewrite = astRewrite.getListRewrite(oldClazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertAfter(newStaticBlock, oldFieldDeclarations.get(oldFieldDeclarations.size() - 1), null);
        return true;
    }

    @Override
    public int check(Statement statement) {
        return 1;
    }

}
