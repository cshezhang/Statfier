package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import static edu.polyu.Util.getDirectMethodOfStatement;

public class NestedClassWrapper extends Mutator {

    private static NestedClassWrapper nestedClassWrapper = new NestedClassWrapper();

    private NestedClassWrapper() {}

    public static NestedClassWrapper getInstance() {
        return nestedClassWrapper;
    }

    public static int nestedClassCounter = 0;

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        MethodDeclaration oldMethod = getDirectMethodOfStatement(sourceStatement);
        if(oldMethod == null) {
            return false;
        }
        TypeDeclaration nestedClass = ast.newTypeDeclaration();
        nestedClass.setName(ast.newSimpleName("subClass" + nestedClassCounter++));
        MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
        nestedClass.bodyDeclarations().add(newMethod);
        astRewrite.replace(oldMethod, nestedClass, null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        return true;
    }

    @Override
    public int getIndex() {
        return 9;
    }
}
