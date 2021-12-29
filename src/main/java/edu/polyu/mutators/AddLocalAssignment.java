package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * @Description: Add assignment mutatant
 * @Author: Vanguard
 * @Date: 2021-08-20 21:08
 */
public class AddLocalAssignment extends Mutator {

    private static final AddLocalAssignment instance = new AddLocalAssignment();

    private AddLocalAssignment() {}

    public static Mutator getInstance() {
        return instance;
    }

    @Override
    public int getIndex() {
        return 2;
    }

    /*
    * Source: Var A = B; => Var A; A = B; VD_Statement => VD + Assignment
    * Including final Var A = B; Attention: Final variable assignment cannot be applied to Array Type.
    * */
    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        VariableDeclarationStatement oldVdStatement = (VariableDeclarationStatement) sourceStatement;
        VariableDeclarationFragment oldFragment = (VariableDeclarationFragment) oldVdStatement.fragments().get(0);
        Expression initializer = oldFragment.getInitializer();
        Expression newInitializer = (Expression) ASTNode.copySubtree(ast, initializer);
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        newVdFragment.setName(ast.newSimpleName(oldFragment.getName().toString()));
        newVdFragment.setExtraDimensions(oldFragment.getExtraDimensions());
        VariableDeclarationStatement newVdStatement = ast.newVariableDeclarationStatement(newVdFragment);
        newVdStatement.setType((Type) ASTNode.copySubtree(ast, oldVdStatement.getType()));
        if(!oldVdStatement.modifiers().isEmpty()) {
            newVdStatement.modifiers().add(ASTNode.copySubtree(ast, (ASTNode) oldVdStatement.modifiers().get(0)));
        }
        Assignment assignment = ast.newAssignment();
        assignment.setLeftHandSide(ast.newSimpleName(oldFragment.getName().toString()));
        assignment.setRightHandSide(newInitializer);
        ExpressionStatement newAssignStatement = ast.newExpressionStatement(assignment);
        ListRewrite listRewrite = astRewrite.getListRewrite(sourceStatement.getParent(), Block.STATEMENTS_PROPERTY);
        try {
            listRewrite.insertAfter(newVdStatement, sourceStatement, null);
            listRewrite.insertAfter(newAssignStatement, newVdStatement, null); // newVdStatement has been inserted into code block.
            listRewrite.remove(sourceStatement, null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) statement;
            // System.out.println(vdStatement.getType());
            if(vdStatement.getType() instanceof ArrayType) {
                if(vdStatement.modifiers().size() > 0) {
                    Modifier modifier = (Modifier) vdStatement.modifiers().get(0);
                    if(modifier.getKeyword().toString().equals("final")) {
                        return false;
                        // final byte[] values={0}; -> final byte[] values; values = {0} is wrong.
                    }
                }
            }
            VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
            if(vdFragment.getInitializer() != null) {
                return true;
            }
        }
        return false;
    }
}
