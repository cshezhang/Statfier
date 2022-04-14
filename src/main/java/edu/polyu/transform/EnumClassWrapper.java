package edu.polyu.transform;

import edu.polyu.analysis.ASTWrapper;
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
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

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
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brotherNode, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        TypeDeclaration clazz = getClassOfStatement(srcNode);
        MethodDeclaration oldMethod = getDirectMethodOfStatement(srcNode);
        EnumConstantDeclaration enumConstant = ast.newEnumConstantDeclaration();
        enumConstant.setName(ast.newSimpleName("RED"));
        EnumDeclaration enumClass = ast.newEnumDeclaration();
        enumClass.setName(ast.newSimpleName("enumClass" + enumCounter));
        enumClass.enumConstants().add(enumConstant);
        ListRewrite listRewrite = astRewrite.getListRewrite(enumClass, enumClass.getBodyDeclarationsProperty());
        if(oldMethod != null) {
            // Here, we think oldNote is located in a method
            List<VariableDeclarationStatement> validVDStatemnets = new ArrayList<>();
            for (ASTNode node : (List<ASTNode>) clazz.bodyDeclarations()) {
                if (node instanceof VariableDeclarationStatement) {
                    validVDStatemnets.add((VariableDeclarationStatement) node);  // copy all vd statements to enum class
                }
            }
            MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
            for (VariableDeclarationStatement vdStatement : validVDStatemnets) {
                listRewrite.insertLast(vdStatement, null);
            }
            listRewrite.insertLast(newMethod, null);
            astRewrite.replace(oldMethod, enumClass, null);
            return true;
        } else {
            // Here, we think oldNode is a FieldDeclaration or Initializer
            if(srcNode instanceof FieldDeclaration) {
                FieldDeclaration newStatement = (FieldDeclaration) ASTNode.copySubtree(ast, srcNode);
                listRewrite.insertLast(newStatement, null);
                astRewrite.replace(srcNode, enumClass, null);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(node instanceof MethodDeclaration) {
            nodes.add(node);
            return nodes;
        }
        TypeDeclaration clazz = getClassOfStatement(node);
        MethodDeclaration method = getDirectMethodOfStatement(node);
        if(method == null) {  // means FieldDeclaration
            nodes.add(node);
            return nodes;
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
                return nodes;
            }
            Type superClazzType = clazz.getSuperclassType();
            if(superClazzType == null) {
                nodes.add(node);
                return nodes;
            }
            if (superClazzType instanceof SimpleType) {
                String name = ((SimpleType) superClazzType).getName().getFullyQualifiedName();
                if(name.contains("Object")) {
                    nodes.add(node);
                    return nodes;
                }
            }
            return nodes;
        } else {
            nodes.add(node);
            return nodes;
        }
    }

}
