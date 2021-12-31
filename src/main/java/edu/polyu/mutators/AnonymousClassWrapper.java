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

    public static int varCounter;

    private static AnonymousClassWrapper instance = new AnonymousClassWrapper();

    public static AnonymousClassWrapper getInstance() {
        return instance;
    }

    private AnonymousClassWrapper() {
        varCounter = 0;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        MethodDeclaration oldMethod = getDirectMethodOfStatement(sourceStatement);
        if(oldMethod == null) {
            return false;
        }
        MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);

        AnonymousClassDeclaration anonymousClassDeclaration = ast.newAnonymousClassDeclaration();
        anonymousClassDeclaration.bodyDeclarations().add(newMethod);
        ClassInstanceCreation instanceCreation = ast.newClassInstanceCreation();
        instanceCreation.setType(ast.newSimpleType(ast.newSimpleName("Object")));
        instanceCreation.setAnonymousClassDeclaration(anonymousClassDeclaration);

        VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
        fragment.setInitializer(instanceCreation);
        fragment.setName(ast.newSimpleName("acw" + varCounter++));

        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fragment);
        fieldDeclaration.setType(ast.newSimpleType(ast.newSimpleName("Object")));
        astRewrite.replace(oldMethod, fieldDeclaration, null);
        return true;
    }

    @Override
    public int check(Statement statement) {
        return 1;
    }

}
