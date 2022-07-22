package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static edu.polyu.analysis.TypeWrapper.createMethodSignature;
import static edu.polyu.analysis.TypeWrapper.getChildrenNodes;
import static edu.polyu.analysis.TypeWrapper.getClassOfStatement;
import static edu.polyu.analysis.TypeWrapper.getDirectMethodOfStatement;
import static edu.polyu.analysis.TypeWrapper.getStatementOfNode;

public class EnumClassWrapper extends Transform {

    private static int enumCounter = 0;

    private static EnumClassWrapper instance = new EnumClassWrapper();

    public static EnumClassWrapper getInstance() {
        return instance;
    }

    private EnumClassWrapper() {}

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brotherNode, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        TypeDeclaration clazz = getClassOfStatement(srcNode);
        List<ASTNode> classModifiers = clazz.modifiers();
        List<String> staticFieldNames = new ArrayList<>();
        for(FieldDeclaration fd : clazz.getFields()) {
            for(Object modifier : fd.modifiers()) {
                if(modifier instanceof Modifier) {
                    if(((Modifier) modifier).isStatic()) {
                        staticFieldNames.add(((VariableDeclarationFragment)fd.fragments().get(0)).getName().getFullyQualifiedName());
                    }
                }
            }
        }
        MethodDeclaration oldMethod = getDirectMethodOfStatement(srcNode);
        EnumConstantDeclaration enumConstant = ast.newEnumConstantDeclaration();
        enumConstant.setName(ast.newSimpleName("RED"));
        EnumDeclaration enumClass = ast.newEnumDeclaration();
        for(ASTNode classModifier : classModifiers) {
            if(classModifier instanceof Modifier) {
                enumClass.modifiers().add(ASTNode.copySubtree(ast, classModifier));
            }
        }
        enumClass.setName(ast.newSimpleName("enumClass" + enumCounter++));
        enumClass.enumConstants().add(enumConstant);
        ListRewrite listRewrite = astRewrite.getListRewrite(enumClass, enumClass.getBodyDeclarationsProperty());
        if(oldMethod != null) {
            if(oldMethod.isConstructor()) {
                return false;
            }
            TypeDeclaration type = getClassOfStatement(srcNode);
            String methodKey = type.getName().toString() + ":" + createMethodSignature(oldMethod);
            for(Map.Entry<String, HashSet<String>> entry : wrapper.getMethod2identifiers().entrySet()) {
                if(!entry.getKey().equals(methodKey) && entry.getValue().contains(oldMethod.getName().getIdentifier())) {
                    return false;
                }
            }
            if(oldMethod.getBody() != null) {
                for (Statement statement : (List<Statement>) oldMethod.getBody().statements()) {
                    List<ASTNode> nodes = getChildrenNodes(statement);
                    for (ASTNode node : nodes) {
                        if (node instanceof SimpleName) {
                            if (!staticFieldNames.contains(((SimpleName) node).getFullyQualifiedName())) {
                                return false;
                            }
                        }
                    }
                }
            }
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
                String varName = ((VariableDeclarationFragment) (((FieldDeclaration) srcNode).fragments().get(0))).getName().getIdentifier();
                for(Map.Entry<String, HashSet<String>> entry : wrapper.getMethod2identifiers().entrySet()) {
                    if(entry.getValue().contains(varName)) {
                        return false;
                    }
                }
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
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        TypeDeclaration clazz = getClassOfStatement(node);
        MethodDeclaration method = getDirectMethodOfStatement(node);
        List<ASTNode> classModifiers = clazz.modifiers();
        for(ASTNode classModifier : classModifiers) {
            if(classModifier instanceof Modifier) {
                if(((Modifier) classModifier).getKeyword().toString().contains("final")) {
                    return nodes;
                }
            }
        }
        List<ASTNode> methodModifiers = (List<ASTNode>) method.modifiers();
        for (ASTNode component : (List<ASTNode>) clazz.bodyDeclarations()) {
            if(component instanceof TypeDeclaration) {
                Type parentType = ((TypeDeclaration) component).getSuperclassType();
                if(parentType instanceof SimpleType && ((SimpleType) parentType).getName().toString().equals(clazz.getName().getIdentifier())) {
                    return nodes;
                }
            }
        }
        if (method == null) {
            if(getStatementOfNode(node) instanceof FieldDeclaration) {
                nodes.add(node);  // FieldDeclaration
            }
            return nodes;
        }
        List<ASTNode> subNodes = getChildrenNodes(method);
        boolean hasThis = false;
        for(ASTNode subNode : subNodes) {
            if(subNode instanceof ThisExpression) {
                hasThis = true;
                break;
            }
        }
        if(hasThis) {
            return nodes;
        }
        boolean isOverride = false;
        for (ASTNode modifier : methodModifiers) {
            if (modifier instanceof MarkerAnnotation) {
                String name = ((MarkerAnnotation) modifier).getTypeName().getFullyQualifiedName();
                if (name.contains("Override")) {
                    isOverride = true;
                    break;
                }
                if(name.contains("Test")) {
                    return nodes;
                }
            }
            if(modifier instanceof Modifier) {
                if(((Modifier) modifier).getKeyword().toString().contains("abstract")) {
                    return nodes;
                }
            }
        }
        if (isOverride) {
            if (clazz.superInterfaceTypes().size() > 0) {
                return nodes;
            }
            Type superClazzType = clazz.getSuperclassType();
            if (superClazzType == null) {
                nodes.add(node);
                return nodes;
            }
            if (superClazzType instanceof SimpleType) {
                String name = ((SimpleType) superClazzType).getName().getFullyQualifiedName();
                if (name.contains("Object")) {
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

    public List<ASTNode> classCheck(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        TypeDeclaration clazz = getClassOfStatement(node);
        MethodDeclaration method = getDirectMethodOfStatement(node);
        for (ASTNode component : (List<ASTNode>) clazz.bodyDeclarations()) {
            if(component instanceof TypeDeclaration) {
                Type parentType = ((TypeDeclaration) component).getSuperclassType();
                if(parentType instanceof SimpleType && ((SimpleType) parentType).getName().toString().equals(clazz.getName().getIdentifier())) {
                    return nodes;
                }
            }
        }
        if(method == null) {  // means FieldDeclaration
            if(getStatementOfNode(node) instanceof FieldDeclaration) {
                nodes.add(node);
            }
            return nodes;
        }
        List<ASTNode> subNodes = getChildrenNodes(method);
        boolean hasThis = false;
        for(ASTNode subNode : subNodes) {
            if(subNode instanceof ThisExpression) {
                hasThis = true;
                break;
            }
        }
        if(hasThis) {
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
