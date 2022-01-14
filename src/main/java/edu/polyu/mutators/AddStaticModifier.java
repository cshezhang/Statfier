package edu.polyu.mutators;

import edu.polyu.FieldMutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

public class AddStaticModifier extends FieldMutator {

    private static final AddStaticModifier instance = new AddStaticModifier();

    private AddStaticModifier() {}

    public static AddStaticModifier getInstance() {
        return instance;
    }

    @Override
    public boolean run(AST ast, ASTRewrite astRewrite, FieldDeclaration oldFieldDeclaration) {
        if(oldFieldDeclaration.modifiers().contains(Modifier.ModifierKeyword.STATIC_KEYWORD)) {
            return false;
        }
        FieldDeclaration newFieldDeclaration;
        try {
            // Failed corner case: float e19=0x0.0p0f; Hence, we add a try-catch here.
            newFieldDeclaration = (FieldDeclaration) ASTNode.copySubtree(ast, oldFieldDeclaration);
        } catch (Exception e) {
            return false;
        }
        newFieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
        astRewrite.replace(oldFieldDeclaration, newFieldDeclaration, null);
        return true;
    }

    @Override
    public String getIndex() {
        return "AddStaticModifier";
    }


}
