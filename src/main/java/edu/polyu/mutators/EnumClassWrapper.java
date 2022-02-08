package edu.polyu.mutators;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.Util.getDirectMethodOfStatement;
import static edu.polyu.Util.getTypeOfStatement;

public class EnumClassWrapper extends Mutator {

    private static int enumCounter = 0;

    private static EnumClassWrapper instance = new EnumClassWrapper();

    public static EnumClassWrapper getInstance() {
        return instance;
    }

    private EnumClassWrapper() {}

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        MethodDeclaration oldMethod = getDirectMethodOfStatement(sourceStatement);
        if(oldMethod == null) {
            return false;
        }
        TypeDeclaration clazz = (TypeDeclaration) oldMethod.getParent();
        List<VariableDeclarationStatement> validVDStatemnets = new ArrayList<>();
        for(ASTNode node : (List<ASTNode>) clazz.bodyDeclarations()) {
            if(node instanceof VariableDeclarationStatement) {
                validVDStatemnets.add((VariableDeclarationStatement) node);
            }
        }
        MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
        EnumConstantDeclaration enumConstant = ast.newEnumConstantDeclaration();
        enumConstant.setName(ast.newSimpleName("RED"));
        EnumDeclaration enumDeclaration = ast.newEnumDeclaration();
        enumDeclaration.setName(ast.newSimpleName("enumClass" + enumCounter));
        enumDeclaration.enumConstants().add(enumConstant);
        for(VariableDeclarationStatement vdStatement : validVDStatemnets) {
            enumDeclaration.bodyDeclarations().add(vdStatement);
        }
        enumDeclaration.bodyDeclarations().add(newMethod);
        astRewrite.replace(oldMethod, enumDeclaration, null);
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
