package edu.polyu.transform;

import edu.polyu.analysis.ASTWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
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
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brother, ASTNode oldStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        MethodDeclaration oldMethod = getDirectMethodOfStatement(oldStatement);
        if(oldMethod.isConstructor()) {
            return false;
        }
        for(Object modifier : oldMethod.modifiers()) {
            if(modifier instanceof Modifier) {
                if(((Modifier) modifier).getKeyword().toString().equals("static")) {
                    return false;
                }
            }
        }
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
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(node instanceof MethodDeclaration) {
            nodes.add(node);
            return nodes;
        }
        TypeDeclaration clazz = getClassOfStatement(node);
        MethodDeclaration method = getDirectMethodOfStatement(node);
        if(method == null) { // means global variable definition
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
