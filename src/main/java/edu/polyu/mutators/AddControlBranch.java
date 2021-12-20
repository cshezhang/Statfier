/*
 * @Description: 
 * @Author: Austin ZHANG
 * @Date: 2021-10-14 09:25:07
 */
package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * @Description: Add control branch
 * @Author: Huaien Zhang
 * @Date: 2021-08-20 18:17
 */
public class AddControlBranch extends Mutator {


    private static AddControlBranch singleInstance = new AddControlBranch();

    public static AddControlBranch getInstance() {
        return singleInstance;
    }

    @Override
    public int getIndex() {
        return 4;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        IfStatement newIfStatement = ast.newIfStatement();
        Block thenBlock = ast.newBlock();
        Block elseBlock = ast.newBlock();
        // false branch is directly copied from source statement
        // true branch considers vd statement because of define range of variable
        // namely, int a = 10; -> int a; if(true) { a = 10; } else { a = 10; }
        if(sourceStatement instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement oldVdStatement = (VariableDeclarationStatement) sourceStatement;
            VariableDeclarationFragment oldFragment = (VariableDeclarationFragment) oldVdStatement.fragments().get(0);
            VariableDeclarationFragment newFragment = ast.newVariableDeclarationFragment();
            newFragment.setName(ast.newSimpleName(oldFragment.getName().toString()));
            newFragment.setExtraDimensions(oldFragment.getExtraDimensions());
            VariableDeclarationStatement newVdStatement = ast.newVariableDeclarationStatement(newFragment);
            newVdStatement.setType((Type) ASTNode.copySubtree(ast, oldVdStatement.getType()));
            Assignment assignment = ast.newAssignment();
            assignment.setLeftHandSide(ast.newSimpleName(oldFragment.getName().toString()));
            Expression e = (Expression) ASTNode.copySubtree(ast, oldFragment.getInitializer());
            assignment.setRightHandSide(e);
            ExpressionStatement thenStatement = ast.newExpressionStatement(assignment);
            ExpressionStatement elseStatement = (ExpressionStatement) ASTNode.copySubtree(ast, thenStatement);
            thenBlock.statements().add(thenStatement);
            elseBlock.statements().add(elseStatement);
            newIfStatement.setThenStatement(thenBlock);
            newIfStatement.setElseStatement(elseBlock);
            newIfStatement.setExpression(ast.newBooleanLiteral(true));
            ListRewrite listRewrite = astRewrite.getListRewrite(brotherStatement.getParent(), Block.STATEMENTS_PROPERTY);
            listRewrite.insertAfter(newVdStatement, sourceStatement, null);
            listRewrite.insertAfter(newIfStatement, newVdStatement, null);
            listRewrite.remove(sourceStatement, null);
        } else {
            Statement thenStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
            Statement elseStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
            thenBlock.statements().add(thenStatement);
            elseBlock.statements().add(elseStatement);
            newIfStatement.setExpression(ast.newBooleanLiteral(true));
            newIfStatement.setThenStatement(thenBlock);
            newIfStatement.setElseStatement(elseBlock);
            astRewrite.replace(sourceStatement, newIfStatement, null);
        }
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) statement;
            if(vdStatement.getType() instanceof ArrayType) {
                if(vdStatement.modifiers().size() > 0) {
                    Modifier modifier = (Modifier) vdStatement.modifiers().get(0);
                    if(modifier.getKeyword().toString().equals("final")) {
                        return false;
                    }
                }
            }
            VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
            if(vdFragment.getInitializer() == null) {
                return false;
            }
        }
        return true;
    }
}
