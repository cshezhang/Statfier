package edu.polyu.transform;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Util.getClassOfStatement;
import static edu.polyu.util.Util.getDirectMethodOfStatement;

public class EnumClassWrapper extends Transform {

    private static int enumCounter = 0;

    private static EnumClassWrapper instance = new EnumClassWrapper();

    public static EnumClassWrapper getInstance() {
        return instance;
    }

    private EnumClassWrapper() {}

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brotherNode, ASTNode oldNode) {
        TypeDeclaration clazz = getClassOfStatement(oldNode);
        MethodDeclaration oldMethod = getDirectMethodOfStatement(oldNode);
        EnumConstantDeclaration enumConstant = ast.newEnumConstantDeclaration();
        enumConstant.setName(ast.newSimpleName("RED"));
        EnumDeclaration enumDeclaration = ast.newEnumDeclaration();
        enumDeclaration.setName(ast.newSimpleName("enumClass" + enumCounter));
        enumDeclaration.enumConstants().add(enumConstant);
        if(oldMethod != null) {
            List<VariableDeclarationStatement> validVDStatemnets = new ArrayList<>();
            for (ASTNode node : (List<ASTNode>) clazz.bodyDeclarations()) {
                if (node instanceof VariableDeclarationStatement) {
                    validVDStatemnets.add((VariableDeclarationStatement) node);
                }
            }
            MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
            for (VariableDeclarationStatement vdStatement : validVDStatemnets) {
                enumDeclaration.bodyDeclarations().add(vdStatement);
            }
            enumDeclaration.bodyDeclarations().add(newMethod);
            astRewrite.replace(oldMethod, enumDeclaration, null);
            return true;
        } else {
            if(oldNode instanceof FieldDeclaration) {
                FieldDeclaration newStatement = (FieldDeclaration) ASTNode.copySubtree(ast, oldNode);
                enumDeclaration.bodyDeclarations().add(newStatement);
                astRewrite.replace(oldNode, enumDeclaration, null);
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
        if(method == null) {  // means FieldDeclaration
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
