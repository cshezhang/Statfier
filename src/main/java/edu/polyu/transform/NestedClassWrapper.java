package edu.polyu.transform;


import edu.polyu.analysis.ASTWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
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
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brother, ASTNode oldStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        MethodDeclaration oldMethod = getDirectMethodOfStatement(oldStatement);
        if(oldMethod.isConstructor()) {
            return false;
        }
        if(oldMethod != null) {
            for(Object modifier : oldMethod.modifiers()) {
                if(modifier instanceof Modifier) {
                    if(((Modifier) modifier).getKeyword().toString().equals("static")) {
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
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(node instanceof MethodDeclaration) {
            nodes.add(node);
            return nodes;
        }
        TypeDeclaration clazz = getClassOfStatement(node);
        MethodDeclaration method = getDirectMethodOfStatement(node);
        if(method == null) {
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
