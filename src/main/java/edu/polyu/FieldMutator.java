package edu.polyu;

import edu.polyu.mutators.AddStaticAssignment;
import edu.polyu.mutators.AddStaticModifier;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

public abstract class FieldMutator {

    private static final List<FieldMutator> transforms = new ArrayList<>();

    public abstract boolean run(AST ast, ASTRewrite astRewrite, FieldDeclaration oldFieldDeclaration);
    public abstract String getIndex();

    static {
        transforms.add(AddStaticAssignment.getInstance());
        transforms.add(AddStaticModifier.getInstance());
    }

    public static List<FieldMutator> getTransforms() {
        return transforms;
    }

}
