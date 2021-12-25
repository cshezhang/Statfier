package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import static edu.polyu.Util.getDirectMethodOfStatement;

public class AnonymousClassWrapper extends Mutator {

    public static int anonymousClassCounter = 0;

    private static AnonymousClassWrapper instance = new AnonymousClassWrapper();

    public static AnonymousClassWrapper getInstance() {
        return instance;
    }

    private AnonymousClassWrapper() {}

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        MethodDeclaration oldMethod = getDirectMethodOfStatement(sourceStatement);
        if(oldMethod == null) {
            return false;
        }
        MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
        VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
        ClassInstanceCreation instanceCreation = ast.newClassInstanceCreation();
        instanceCreation.setType(ast.newSimpleType(ast.newSimpleName("Object")));
        AnonymousClassDeclaration anonymousClassDeclaration = ast.newAnonymousClassDeclaration();
        anonymousClassDeclaration.bodyDeclarations().add(newMethod);
        fragment.setInitializer(instanceCreation);
        fragment.setName(ast.newSimpleName("obj" + anonymousClassCounter++));
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fragment);
        fieldDeclaration.setType(ast.newSimpleType(ast.newSimpleName("Object")));
        astRewrite.replace(oldMethod, fieldDeclaration, null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        return false;
    }

    @Override
    public int getIndex() {
        return 7;
    }
}
