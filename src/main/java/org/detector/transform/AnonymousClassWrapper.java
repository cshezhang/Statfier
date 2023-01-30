package org.detector.transform;

import org.detector.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
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

import static org.detector.analysis.TypeWrapper.getChildrenNodes;
import static org.detector.analysis.TypeWrapper.getStatementOfNode;

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
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brother, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        TypeDeclaration oldClass = TypeWrapper.getClassOfNode(srcNode);
        MethodDeclaration oldMethod = TypeWrapper.getDirectMethodOfNode(srcNode);
        AnonymousClassDeclaration anonymousClassDeclaration = ast.newAnonymousClassDeclaration();
        ClassInstanceCreation instanceCreation = ast.newClassInstanceCreation();
        instanceCreation.setType(ast.newSimpleType(ast.newSimpleName("Object")));
        if(oldMethod != null) {
            if(oldMethod.isConstructor()) {
                return false;
            }
            String methodKey = oldClass.getName().toString() + ":" + TypeWrapper.createMethodSignature(oldMethod);
            for(Map.Entry<String, HashSet<String>> entry : wrapper.getMethod2identifiers().entrySet()) {
                if(!entry.getKey().equals(methodKey) && entry.getValue().contains(oldMethod.getName().getIdentifier())) {
                    return false;
                }
            }
            for(Object modifier : oldMethod.modifiers()) {
                if(modifier instanceof Modifier) {
                    if(((Modifier) modifier).getKeyword().toString().equals("static")) {
                        return false;
                    }
                }
            }
            MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
            anonymousClassDeclaration.bodyDeclarations().add(newMethod);
            instanceCreation.setAnonymousClassDeclaration(anonymousClassDeclaration);
            // Init value of new FieldDeclaration
            VariableDeclarationFragment newFragment = ast.newVariableDeclarationFragment();
            newFragment.setInitializer(instanceCreation);
            newFragment.setName(ast.newSimpleName("anonWrap" + varCounter++));
            // insert new FieldStatement containing Anonymous class
            FieldDeclaration newClass = ast.newFieldDeclaration(newFragment);
            newClass.setType(ast.newSimpleType(ast.newSimpleName("Object")));
            astRewrite.replace(oldMethod, newClass, null);
            return true;
        } else {
            if(srcNode instanceof FieldDeclaration) {
                FieldDeclaration oldFieldDeclaration = (FieldDeclaration) srcNode;
                String varName = ((VariableDeclarationFragment) (((FieldDeclaration) srcNode).fragments().get(0))).getName().getIdentifier();
                for(Map.Entry<String, HashSet<String>> entry : wrapper.getMethod2identifiers().entrySet()) {
                    if(entry.getValue().contains(varName)) {
                        return false;
                    }
                }
                FieldDeclaration newFieldDeclaration = (FieldDeclaration) ASTNode.copySubtree(ast, oldFieldDeclaration);
                anonymousClassDeclaration.bodyDeclarations().add(newFieldDeclaration);
                instanceCreation.setAnonymousClassDeclaration(anonymousClassDeclaration);
                // Init value of new FieldDeclaration
                VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
                fragment.setInitializer(instanceCreation);
                fragment.setName(ast.newSimpleName("anonWrap" + varCounter++));
                // insert new FieldStatement containing Anonymous class
                FieldDeclaration newClass = ast.newFieldDeclaration(fragment);
                newClass.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
                newClass.setType(ast.newSimpleType(ast.newSimpleName("Object")));
                astRewrite.replace(oldFieldDeclaration, newClass, null);
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
//        List<ASTNode> typeModifiers = clazz.modifiers();
//        for(ASTNode modifier : typeModifiers) {
//            if(modifier instanceof Modifier) {
//                if(modifier.toString().contains("final")) {
//                    return nodes;
//                }
//            }
//        }
        MethodDeclaration method = TypeWrapper.getDirectMethodOfNode(node);
        if (method == null) {
            ASTNode statement = getStatementOfNode(node);
            if(statement instanceof FieldDeclaration) {
                if(((FieldDeclaration) statement).getType().toString().toLowerCase().contains("logger")) {
                    return nodes;
                }
                VariableDeclarationFragment fragment = (VariableDeclarationFragment) ((FieldDeclaration) statement).fragments().get(0);
                String varName = fragment.getName().getIdentifier();
                if(varName.equals("serialVersionUID")) {
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
        List<ASTNode> modifiers = (List<ASTNode>) method.modifiers();
        for (ASTNode component : (List<ASTNode>) clazz.bodyDeclarations()) {
            if(component instanceof TypeDeclaration) {
                Type parentType = ((TypeDeclaration) component).getSuperclassType();
                if(parentType instanceof SimpleType && ((SimpleType) parentType).getName().toString().equals(clazz.getName().getIdentifier())) {
                    return nodes;
                }
            }
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
        for (ASTNode modifier : modifiers) {
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
