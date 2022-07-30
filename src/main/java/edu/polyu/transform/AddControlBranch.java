package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.analysis.TypeWrapper.isLiteral;
import static edu.polyu.util.Utility.file2row;

/**
 * @Description: Add control branch
 * @Author: Vanguard
 * @Date: 2021-08-20 18:17
 */
public class AddControlBranch extends Transform {

    private static final AddControlBranch singleInstance = new AddControlBranch();
    private static int varCounter;

    public static AddControlBranch getInstance() {
        return singleInstance;
    }

    private AddControlBranch() {
        varCounter = 0;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brotherNode, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        IfStatement newIfStatement = ast.newIfStatement();
        Block thenBlock = ast.newBlock();
        Block elseBlock = ast.newBlock();
        // False branch is directly cloned from source statement
        // True branch considers vd statement because of define range of variable
        // Namely, int a = 10; -> int a; if(true) { a = 10; } else { a = 10; }
        VariableDeclarationFragment newBoolVdFragment = ast.newVariableDeclarationFragment();
        String varName = String.format("acb%d", varCounter++);
        newBoolVdFragment.setName(ast.newSimpleName(varName));
        newBoolVdFragment.setInitializer(ast.newBooleanLiteral(true));
        VariableDeclarationStatement newBoolVdStatement = ast.newVariableDeclarationStatement(newBoolVdFragment);
        newBoolVdStatement.setType(ast.newPrimitiveType(PrimitiveType.BOOLEAN));
        // Final modifier can be added by a specific transformation
        int startLine = wrapper.getCompilationUnit().getLineNumber(srcNode.getStartPosition());
        int endLine = wrapper.getCompilationUnit().getLineNumber(srcNode.getStartPosition() + srcNode.getLength());
        List<Integer> rows = file2row.get(wrapper.getParentWrapper().getFilePath());
        for(Integer row : rows) {
            if(row >= startLine && row <= endLine) {
                wrapper.expectedNumbers++;
            }
        }
        if(srcNode instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement oldVdStatement = (VariableDeclarationStatement) srcNode;
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
            ListRewrite listRewrite = astRewrite.getListRewrite(brotherNode.getParent(), Block.STATEMENTS_PROPERTY);
            listRewrite.insertAfter(newVdStatement, srcNode, null);  // Here needs block, but complex to modify, so add it before brother
            listRewrite.insertAfter(newBoolVdStatement, newVdStatement, null);
            listRewrite.insertAfter(newIfStatement, newBoolVdStatement, null);
            listRewrite.remove(srcNode, null);
        } else {
            Statement thenStatement = (Statement) ASTNode.copySubtree(ast, srcNode);
            Statement elseStatement = (Statement) ASTNode.copySubtree(ast, srcNode);
            thenBlock.statements().add(thenStatement);
            elseBlock.statements().add(elseStatement);
            newIfStatement.setExpression(ast.newSimpleName(varName));
            newIfStatement.setThenStatement(thenBlock);
            newIfStatement.setElseStatement(elseBlock);
            Block newBlock = ast.newBlock();
            newBlock.statements().add(newBoolVdStatement);
            newBlock.statements().add(newIfStatement);
            astRewrite.replace(srcNode, newBlock, null);
        }
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if (isLiteral(node)) {
            return nodes;
        }
        if (node instanceof FieldDeclaration || node instanceof MethodDeclaration || node instanceof SuperConstructorInvocation ||
                node instanceof EmptyStatement || node instanceof ReturnStatement) {
            return nodes;
        }
        if(node instanceof Statement) {
            if (node instanceof VariableDeclarationStatement) {
                VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) node;
                if (vdStatement.getType() instanceof ArrayType) {
                    if (vdStatement.modifiers().size() > 0) {
                        Modifier modifier = (Modifier) vdStatement.modifiers().get(0);
                        if (modifier.getKeyword().toString().equals("final")) {
                            return nodes;
                        }
                    }
                }
                VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) node).fragments().get(0);
                if (vdFragment.getInitializer() == null) {
                    return nodes;
                }
            }
            nodes.add(node);
        }
        return nodes;
    }
}
