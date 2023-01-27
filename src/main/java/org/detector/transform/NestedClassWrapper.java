package org.detector.transform;

import org.detector.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
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
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.detector.analysis.TypeWrapper.getChildrenNodes;

public class NestedClassWrapper extends Transform {

    private static NestedClassWrapper nestedClassWrapper = new NestedClassWrapper();

    private NestedClassWrapper() {}

    public static NestedClassWrapper getInstance() {
        return nestedClassWrapper;
    }

    public static int nestedClassCounter = 0;

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brother, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        MethodDeclaration oldMethod = TypeWrapper.getDirectMethodOfNode(srcNode);
        TypeDeclaration nestedClass = ast.newTypeDeclaration();
        nestedClass.setName(ast.newSimpleName("SubClass" + nestedClassCounter++));
        TypeDeclaration type = TypeWrapper.getClassOfNode(srcNode);
        for(ASTNode classModifier : (List<ASTNode>) type.modifiers()) {
            if(classModifier instanceof Modifier) {
                nestedClass.modifiers().add(ASTNode.copySubtree(ast, classModifier));
            }
        }
        if (oldMethod != null) {
            if (oldMethod.isConstructor()) {
                return false;
            }
            String methodKey = type.getName().toString() + ":" + TypeWrapper.createMethodSignature(oldMethod);
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
        TypeDeclaration clazz = TypeWrapper.getClassOfNode(node);
        if(!TypeWrapper.checkClassProperty(clazz)) {
            return nodes;
        }
        MethodDeclaration method = TypeWrapper.getDirectMethodOfNode(node);
        if (method == null) {
            ASTNode statement = TypeWrapper.getStatementOfNode(node);
            if(statement instanceof FieldDeclaration) {
                if(((FieldDeclaration) statement).getType().toString().toLowerCase().contains("logger")) {
                    return nodes;
                }
                VariableDeclarationFragment fragment = (VariableDeclarationFragment) ((FieldDeclaration) statement).fragments().get(0);
                String varName = fragment.getName().getIdentifier();
                if(varName.equals("serialVersionUID") || varName.equalsIgnoreCase(clazz.getName().getIdentifier())) {
                    return nodes;
                }
                for(MethodDeclaration methodDeclaration : clazz.getMethods()) {
                    if(methodDeclaration.getName().getIdentifier().equals(varName)) {
                        return nodes;
                    }
                }
                List<ASTNode> subNodes = getChildrenNodes(node);
                for(ASTNode subNode : subNodes) {
                    if(subNode instanceof ThisExpression) {
                        return nodes;
                    }
                }
                nodes.add(node);  // FieldDeclaration
            }
            return nodes;
        }
        String methodName = method.getName().getIdentifier();
        if(!method.isConstructor() && methodName.equals(clazz.getName().getIdentifier())) {
            return nodes;
        }
        if(methodName.equals("equals") || methodName.equals("hashCode") || methodName.equals("toString") || methodName.equals("clone") || methodName.equals("compareTo")) {
            return nodes;
        }
        if(methodName.toLowerCase().startsWith("test") || methodName.startsWith("get") || methodName.startsWith("set")) {
            return nodes;
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
        List<ASTNode> subNodes = TypeWrapper.getChildrenNodes(method);
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
                }
                if(name.contains("Test") || name.contains("UiThread")) {
                    return nodes;
                }
            }
            if(modifier instanceof Modifier) {
                String modifierName = ((Modifier) modifier).getKeyword().toString();
                if(modifierName.contains("abstract") || modifierName.contains("synchronized")) {
                    return nodes;
                }
            }
        }
        if(!isOverride) {
            nodes.add(node);
        }
        return nodes;
//        if (isOverride) {
//            if (clazz.superInterfaceTypes().size() > 0) {
//                return nodes;
//            }
//            Type superClazzType = clazz.getSuperclassType();
//            if (superClazzType == null) {
//                nodes.add(node);
//                return nodes;
//            }
//            if (superClazzType instanceof SimpleType) {
//                String name = ((SimpleType) superClazzType).getName().getFullyQualifiedName();
//                if (name.contains("Object")) {
//                    nodes.add(node);
//                    return nodes;
//                }
//            }
//            return nodes;
//        } else {
//            nodes.add(node);
//            return nodes;
//        }
    }

}
