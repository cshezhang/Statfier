package edu.polyu.transform;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.List;

import static edu.polyu.util.Util.getClassOfStatement;
import static edu.polyu.util.Util.getDirectMethodOfStatement;

public class AnonymousClassWrapper extends Transform {

    private static int varCounter;

    private static AnonymousClassWrapper instance = new AnonymousClassWrapper();

    public static AnonymousClassWrapper getInstance() {
        return instance;
    }

    private AnonymousClassWrapper() {
        varCounter = 0;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brother, ASTNode oldStatement) {
        MethodDeclaration oldMethod = getDirectMethodOfStatement(oldStatement);
        AnonymousClassDeclaration anonymousClassDeclaration = ast.newAnonymousClassDeclaration();
        ClassInstanceCreation instanceCreation = ast.newClassInstanceCreation();
        instanceCreation.setType(ast.newSimpleType(ast.newSimpleName("Object")));
        if(oldMethod != null) {
            MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
            anonymousClassDeclaration.bodyDeclarations().add(newMethod);
            instanceCreation.setAnonymousClassDeclaration(anonymousClassDeclaration);
            // Init value of new FieldDeclaration
            VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
            fragment.setInitializer(instanceCreation);
            fragment.setName(ast.newSimpleName("acw" + varCounter++));
            // insert new FieldStatement containing Anonymous class
            FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fragment);
            fieldDeclaration.setType(ast.newSimpleType(ast.newSimpleName("Object")));
            astRewrite.replace(oldMethod, fieldDeclaration, null);
            return true;
        } else {
            if(oldStatement instanceof FieldDeclaration) {
                FieldDeclaration newFieldDeclaration = (FieldDeclaration) ASTNode.copySubtree(ast, oldStatement);
                anonymousClassDeclaration.bodyDeclarations().add(newFieldDeclaration);
                instanceCreation.setAnonymousClassDeclaration(anonymousClassDeclaration);
                // Init value of new FieldDeclaration
                VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
                fragment.setInitializer(instanceCreation);
                fragment.setName(ast.newSimpleName("acw" + varCounter++));
                // insert new FieldStatement containing Anonymous class
                FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fragment);
                fieldDeclaration.setType(ast.newSimpleType(ast.newSimpleName("Object")));
                astRewrite.replace(oldStatement, fieldDeclaration, null);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int check(ASTNode node) {
        if(node instanceof MethodDeclaration) {
            return 1;
        }
        TypeDeclaration clazz = getClassOfStatement(node);
        MethodDeclaration method = getDirectMethodOfStatement(node);
        if(method == null) { // means global variable definition
            return 1;
        }
        boolean isOverride = false;
        for(ASTNode modifier : (List<ASTNode>) method.modifiers()) {
            if(modifier instanceof MarkerAnnotation) {
                String name = ((MarkerAnnotation) modifier).getTypeName().getFullyQualifiedName();
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
