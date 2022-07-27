package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.analysis.TypeWrapper.getClassOfNode;
import static edu.polyu.analysis.TypeWrapper.getStatementOfNode;

/**
 * @Description: static int a = b; -> static int a; static {a = b;}
 * @Author: Vanguard
 * @Date: 2021-12-15 12:49
 */
public class AddGlobalAssignment extends Transform {

    private static final AddGlobalAssignment instance = new AddGlobalAssignment();
    private AddGlobalAssignment() {}

    public static AddGlobalAssignment getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brotherStatement, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        TypeDeclaration oldClazz = getClassOfNode(srcNode);
        if (!(targetNode instanceof FieldDeclaration)) {
            return false;
        }
        FieldDeclaration oldFD = (FieldDeclaration) targetNode;
        VariableDeclarationFragment oldVdFragment = (VariableDeclarationFragment) oldFD.fragments().get(0);
        Expression initializer = oldVdFragment.getInitializer();
        if (initializer == null) {
            return false;
        }
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        newVdFragment.setName(ast.newSimpleName(oldVdFragment.getName().getIdentifier()));
        newVdFragment.setExtraDimensions(oldVdFragment.getExtraDimensions());
        FieldDeclaration newFD = ast.newFieldDeclaration(newVdFragment);
        newFD.setType((Type) ASTNode.copySubtree(ast, oldFD.getType()));
        for (ASTNode modifier : (List<ASTNode>) oldFD.modifiers()) {
            newFD.modifiers().add(ASTNode.copySubtree(ast, modifier));
        }
        Assignment assignment = ast.newAssignment();
        assignment.setLeftHandSide(ast.newSimpleName(oldVdFragment.getName().getIdentifier()));
        assignment.setRightHandSide((Expression) ASTNode.copySubtree(ast, oldVdFragment.getInitializer()));
        assignment.setOperator(Assignment.Operator.ASSIGN);
        ExpressionStatement newAssignment = ast.newExpressionStatement(assignment);
        Initializer newInit = ast.newInitializer();
        newInit.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
        Block block = ast.newBlock();
        block.statements().add(newAssignment);
        newInit.setBody(block);
        ListRewrite bodyRewrite = astRewrite.getListRewrite(oldClazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        astRewrite.replace(oldFD, newFD, null);
        bodyRewrite.insertAfter(newInit, newFD, null);
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        ASTNode statement = getStatementOfNode(node);
        if (statement != null && statement instanceof FieldDeclaration) {
            if(((FieldDeclaration) statement).modifiers().contains(Modifier.ModifierKeyword.STATIC_KEYWORD)) {
                nodes.add(statement);
            }
        }
        return nodes;
    }

}
