package edu.polyu.mutators;

<<<<<<< HEAD
import edu.polyu.Mutator;
=======
>>>>>>> d2a3d5d792e1b70378656198bac2f3a1c133ad84
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.List;

import static edu.polyu.Util.getDirectMethodOfStatement;
import static edu.polyu.Util.getTypeOfStatement;

public class NestedClassWrapper extends Mutator {

    private static NestedClassWrapper nestedClassWrapper = new NestedClassWrapper();

    private NestedClassWrapper() {}

    public static NestedClassWrapper getInstance() {
        return nestedClassWrapper;
    }

    public static int nestedClassCounter = 0;

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        MethodDeclaration oldMethod = getDirectMethodOfStatement(sourceStatement);
        if(oldMethod == null) {
            return false;
        }
        TypeDeclaration nestedClass = ast.newTypeDeclaration();
        nestedClass.setName(ast.newSimpleName("subClass" + nestedClassCounter++));
        MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, oldMethod);
        nestedClass.bodyDeclarations().add(newMethod);
        astRewrite.replace(oldMethod, nestedClass, null);
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
