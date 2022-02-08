package edu.polyu.mutators;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.List;

import static edu.polyu.Util.getDirectMethodOfStatement;
import static edu.polyu.Util.getTypeOfStatement;

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
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
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
        TypeDeclaration clazz = getTypeOfStatement(statement);
        MethodDeclaration method = getDirectMethodOfStatement(statement);
        if(method == null) {
            return 1;
        }
        boolean isOverride = false;
        for(ASTNode node : (List<ASTNode>) method.modifiers()) {
            if(node instanceof MarkerAnnotation) {
                String name = ((MarkerAnnotation) node).getTypeName().getFullyQualifiedName();
                if(name.contains("Override")) {
                    isOverride = true;
                    break;
                }
            }
        }
        if(isOverride) {
            if(clazz.superInterfaceTypes().size() > 0) {
                return 0;
            }
            Type superClazzType = clazz.getSuperclassType();
            if(superClazzType == null) {
                return 1;
            }
            if (superClazzType instanceof SimpleType) {
                String name = ((SimpleType) superClazzType).getName().getFullyQualifiedName();
                if(name.contains("Object")) {
                    return 1;
                }
            }
            return 0;
        } else {
            return 1;
        }
    }

}
