package edu.polyu.transform;


import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static edu.polyu.analysis.TypeWrapper.createMethodSignature;
import static edu.polyu.analysis.TypeWrapper.getChildrenNodes;
import static edu.polyu.analysis.TypeWrapper.getClassOfStatement;
import static edu.polyu.analysis.TypeWrapper.getDirectMethodOfStatement;
import static edu.polyu.analysis.TypeWrapper.getStatementOfNode;

public class NestedClassWrapper extends Transform {

    private static NestedClassWrapper nestedClassWrapper = new NestedClassWrapper();

    private NestedClassWrapper() {
    }

    public static NestedClassWrapper getInstance() {
        return nestedClassWrapper;
    }

    public static int nestedClassCounter = 0;

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brother, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        MethodDeclaration oldMethod = getDirectMethodOfStatement(srcNode);
        if (oldMethod != null) {
            if (oldMethod.isConstructor()) {
                return false;
            }
            TypeDeclaration type = getClassOfStatement(srcNode);
            String methodKey = type.getName().toString() + ":" + createMethodSignature(oldMethod);
            for(Map.Entry<String, HashSet<String>> entry : wrapper.getMethod2identifiers().entrySet()) {
                if(!entry.getKey().equals(methodKey) && entry.getValue().contains(oldMethod.getName().getIdentifier())) {
                    return false;
                }
            }
            for (Object modifier : oldMethod.modifiers()) {
                if (modifier instanceof Modifier) {
                    if (((Modifier) modifier).getKeyword().toString().equals("static")) {
                        return false;
                    }
                }
            }
            TypeDeclaration nestedClass = ast.newTypeDeclaration();
            nestedClass.setName(ast.newSimpleName("subClass" + nestedClassCounter++));
            MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
            nestedClass.bodyDeclarations().add(newMethod);
            astRewrite.replace(oldMethod, nestedClass, null);
            return true;
        } else {
            if (srcNode instanceof FieldDeclaration) {
                String varName = ((VariableDeclarationFragment) (((FieldDeclaration) srcNode).fragments().get(0))).getName().getIdentifier();
                for(Map.Entry<String, HashSet<String>> entry : wrapper.getMethod2identifiers().entrySet()) {
                    if(entry.getValue().contains(varName)) {
                        return false;
                    }
                }
                TypeDeclaration nestedClass = ast.newTypeDeclaration();
                nestedClass.setName(ast.newSimpleName("subClass" + nestedClassCounter++));
                FieldDeclaration newFieldDeclaration = (FieldDeclaration) ASTNode.copySubtree(ast, srcNode);
                nestedClass.bodyDeclarations().add(newFieldDeclaration);
                astRewrite.replace(srcNode, nestedClass, null);
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

}
