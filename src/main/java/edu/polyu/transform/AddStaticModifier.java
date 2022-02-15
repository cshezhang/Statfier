package edu.polyu.transform;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

public class AddStaticModifier extends Transform {

    private static final AddStaticModifier instance = new AddStaticModifier();

    private AddStaticModifier() {}

    public static Transform getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brotherStatement, ASTNode oldStatement) {
        FieldDeclaration oldFieldDeclaration = (FieldDeclaration) oldStatement;
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
    public int check(ASTNode statement) {
        if(statement instanceof FieldDeclaration) {
            return 1;
        } else {
            return 0;
        }
    }

}
