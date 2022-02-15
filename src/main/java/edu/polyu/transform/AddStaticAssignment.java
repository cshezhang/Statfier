package edu.polyu.transform;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * Description: static int a = XX; -> static int a; static { a = 10; }
 * Author: Vanguard
 * Date: 2022-01-04 16:20
 */
public class AddStaticAssignment extends Transform {

    private static final AddStaticAssignment instance = new AddStaticAssignment();

    private AddStaticAssignment() {}

    public static Transform getInstance() {
        return instance;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brotherStatement, ASTNode oldStatement) {
        FieldDeclaration oldFieldDeclaration = (FieldDeclaration) oldStatement;
        if(!oldFieldDeclaration.modifiers().contains(Modifier.ModifierKeyword.STATIC_KEYWORD)) {
            return false;
        }
        VariableDeclarationFragment oldVdFragment = (VariableDeclarationFragment) oldFieldDeclaration.fragments().get(0);
        FieldDeclaration newFieldDeclaration = (FieldDeclaration) ASTNode.copySubtree(ast, oldFieldDeclaration);
        VariableDeclarationFragment newVdFragment = (VariableDeclarationFragment) newFieldDeclaration.fragments().get(0);
        if(newVdFragment.getInitializer() != null) {
            astRewrite.replace(newVdFragment.getInitializer(), null, null);
        }
        Assignment assignment = ast.newAssignment();
        assignment.setLeftHandSide((SimpleName) ASTNode.copySubtree(ast, oldVdFragment.getName()));
        assignment.setRightHandSide((Expression) ASTNode.copySubtree(ast, oldVdFragment.getInitializer()));
        ExpressionStatement newAssignmentStatement = ast.newExpressionStatement(assignment);
        Block newStaticBlock = ast.newBlock();
        newStaticBlock.statements().add(newAssignmentStatement);
        TypeDeclaration clazz = (TypeDeclaration) oldFieldDeclaration.getParent();
        int pos = clazz.bodyDeclarations().indexOf(oldFieldDeclaration);
        if(pos == -1) {
            System.err.println("Class and Type Declaration are not matched!");
            System.exit(-1);
        }
        clazz.bodyDeclarations().add(pos + 1, newFieldDeclaration);
        clazz.bodyDeclarations().add(pos + 2, newStaticBlock);
        clazz.bodyDeclarations().remove(pos);
        return true;
    }

    @Override
    public int check(ASTNode statement) {
        if(statement instanceof FieldDeclaration) {
            return 1;
        } else {
            return 0;
        }
    }

}
