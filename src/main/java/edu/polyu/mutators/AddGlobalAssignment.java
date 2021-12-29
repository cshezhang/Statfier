package edu.polyu.mutators;

import edu.polyu.Mutator;
import edu.polyu.Util;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
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

public class AddGlobalAssignment extends Mutator {

    private static final AddGlobalAssignment instance = new AddGlobalAssignment();
    private AddGlobalAssignment() {}

    public static AddGlobalAssignment getInstance() {
        return instance;
    }

    @Override
    public int getIndex() {
        return 2;
    }

    /*
     * Attention: For FieldDeclaration
     * Source: Var A = B; => Var A; A = B; VD_Statement => VD + Assignment
     * Including final Var A = B; Attention: Final variable assignment cannot be applied to Array Type.
     * */
    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        TypeDeclaration clazz = Util.getTypeOfStatement(sourceStatement);
        List<ASTNode> nodes = clazz.bodyDeclarations();
        List<FieldDeclaration> fieldDeclarations = new ArrayList<>();
        for(int i = 0; i < nodes.size(); i++) {
            ASTNode node = nodes.get(i);
            if(node instanceof FieldDeclaration) {
                FieldDeclaration fieldDeclaration = (FieldDeclaration) node;
                List<ASTNode> modifiers = fieldDeclaration.modifiers();
                if(modifiers.contains(Modifier.ModifierKeyword.STATIC_KEYWORD)) {
                    VariableDeclarationFragment fragment = (VariableDeclarationFragment) fieldDeclaration.fragments().get(0);
                    if (fragment.getInitializer() != null) {
                        fieldDeclarations.add(fieldDeclaration);
                    }
                }
            }
        }
        List<FieldDeclaration> targetFields = new ArrayList<>();
        List<ASTNode> children = Util.getChildrenNodes(sourceStatement);
        for(ASTNode node : children) {
            if(node instanceof SimpleName) {
                for(FieldDeclaration fieldDeclaration : fieldDeclarations) {
                    VariableDeclarationFragment fragment = (VariableDeclarationFragment) fieldDeclaration.fragments().get(0);
                    if(fragment.getName().getIdentifier().equals(((SimpleName) node).getIdentifier())) {
                        targetFields.add(fieldDeclaration);
                    }
                }
            }
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
        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertAfter(newStaticBlock, fieldDeclarations.get(fieldDeclarations.size() - 1), null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) statement;
            // System.out.println(vdStatement.getType());
            if(vdStatement.getType() instanceof ArrayType) {
                if(vdStatement.modifiers().size() > 0) {
                    Modifier modifier = (Modifier) vdStatement.modifiers().get(0);
                    if(modifier.getKeyword().toString().equals("final")) {
                        return false;
                        // final byte[] values={0}; -> final byte[] values; values = {0} is wrong.
                    }
                }
            }
            VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
            if(vdFragment.getInitializer() != null) {
                return true;
            }
        }
        return false;
    }

}
