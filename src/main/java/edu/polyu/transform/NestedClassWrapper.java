package edu.polyu.transform;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.List;

import static edu.polyu.util.Util.getClassOfStatement;
import static edu.polyu.util.Util.getDirectMethodOfStatement;

public class NestedClassWrapper extends Transform {

    private static NestedClassWrapper nestedClassWrapper = new NestedClassWrapper();

    private NestedClassWrapper() {}

    public static NestedClassWrapper getInstance() {
        return nestedClassWrapper;
    }

    public static int nestedClassCounter = 0;

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brother, ASTNode oldStatement) {
        MethodDeclaration oldMethod = getDirectMethodOfStatement(oldStatement);
        if(oldMethod != null) {
            TypeDeclaration nestedClass = ast.newTypeDeclaration();
            nestedClass.setName(ast.newSimpleName("subClass" + nestedClassCounter++));
            MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
            nestedClass.bodyDeclarations().add(newMethod);
            astRewrite.replace(oldMethod, nestedClass, null);
            return true;
        } else {
            if(oldStatement instanceof FieldDeclaration) {
                TypeDeclaration nestedClass = ast.newTypeDeclaration();
                nestedClass.setName(ast.newSimpleName("subClass" + nestedClassCounter++));
                FieldDeclaration newFieldDeclaration = (FieldDeclaration) ASTNode.copySubtree(ast, oldStatement);
                nestedClass.bodyDeclarations().add(newFieldDeclaration);
                astRewrite.replace(oldStatement, nestedClass, null);
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
        if(method == null) {
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
