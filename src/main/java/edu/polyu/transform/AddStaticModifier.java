package edu.polyu.transform;

import edu.polyu.analysis.ASTWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

public class AddStaticModifier extends Transform {

    private static final AddStaticModifier instance = new AddStaticModifier();

    private AddStaticModifier() {}

    public static Transform getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brotherNode, ASTNode oldNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        FieldDeclaration oldFieldDeclaration = (FieldDeclaration) oldNode;
        for(ASTNode modifier : (List<ASTNode>) oldFieldDeclaration.modifiers()) {
            if(modifier instanceof Modifier && ((Modifier) modifier).getKeyword().toString().equals("static")) {
                return false;
            }
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
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(node instanceof FieldDeclaration) {
            nodes.add(node);
        }
        return nodes;
    }

}
