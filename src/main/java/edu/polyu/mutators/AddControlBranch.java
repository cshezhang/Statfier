package edu.polyu.mutators;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * @Description: Add control branch
 * @Author: Vanguard
 * @Date: 2021-08-20 18:17
 */
public class AddControlBranch extends Mutator {


    private static final AddControlBranch singleInstance = new AddControlBranch();
    private static int varCounter;

    public static AddControlBranch getInstance() {
        return singleInstance;
    }

    private AddControlBranch() {
        varCounter = 0;
    }

    @Override
    public boolean run(int index, AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        IfStatement newIfStatement = ast.newIfStatement();
        Block thenBlock = ast.newBlock();
        Block elseBlock = ast.newBlock();
        // False branch is directly cloned from source statement
        // True branch considers vd statement because of define range of variable
        // Namely, int a = 10; -> int a; if(true) { a = 10; } else { a = 10; }
        VariableDeclarationFragment newBoolVdFragment = ast.newVariableDeclarationFragment();
        String varName = String.format("acb%d", varCounter);
        newBoolVdFragment.setName(ast.newSimpleName(varName));
        newBoolVdFragment.setInitializer(ast.newBooleanLiteral(true));
        VariableDeclarationStatement newBoolVdStatement = ast.newVariableDeclarationStatement(newBoolVdFragment);
        newBoolVdStatement.setType(ast.newPrimitiveType(PrimitiveType.BOOLEAN));
        // Final modifier can be added by a specific transformation
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
            newIfStatement.setExpression(ast.newSimpleName(varName));
            ListRewrite listRewrite = astRewrite.getListRewrite(brotherStatement.getParent(), Block.STATEMENTS_PROPERTY);
            listRewrite.insertAfter(newVdStatement, sourceStatement, null);  // Here needs block, but complex to modify, so add it before brother
            listRewrite.insertAfter(newBoolVdStatement, newVdStatement, null);
            listRewrite.insertAfter(newIfStatement, newBoolVdStatement, null);
            listRewrite.remove(sourceStatement, null);
        } else {
            Statement thenStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
            Statement elseStatement = (Statement) ASTNode.copySubtree(ast, sourceStatement);
            thenBlock.statements().add(thenStatement);
            elseBlock.statements().add(elseStatement);
            newIfStatement.setExpression(ast.newSimpleName(varName));
            newIfStatement.setThenStatement(thenBlock);
            newIfStatement.setElseStatement(elseBlock);
            Block newBlock = ast.newBlock();
            newBlock.statements().add(newBoolVdStatement);
            newBlock.statements().add(newIfStatement);
            astRewrite.replace(sourceStatement, newBlock, null);
        }
        return true;
    }

    @Override
    public int check(Statement statement) {
        if(statement instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) statement;
            if(vdStatement.getType() instanceof ArrayType) {
                if(vdStatement.modifiers().size() > 0) {
                    Modifier modifier = (Modifier) vdStatement.modifiers().get(0);
                    if(modifier.getKeyword().toString().equals("final")) {
                        return 0;
                    }
                }
            }
            VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
            if(vdFragment.getInitializer() == null) {
                return 0;
            }
        }
        return 1;
    }
}
