package org.detector.transform;


import org.detector.analysis.TypeWrapper;
import org.detector.analysis.LoopStatement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static org.detector.analysis.TypeWrapper.getChildrenNodes;

/**
 * @Description: transfer local variable declaration to global scope in class, and this mutator contains static modifier
 * @Author: RainyD4y
 * @Date: 2021-10-05 12:49
 */
public class TransferLocalVarToStaticGlobal extends Transform {

    private static int varCounter;
    private static TransferLocalVarToStaticGlobal instance = new TransferLocalVarToStaticGlobal();

    private TransferLocalVarToStaticGlobal() {
        varCounter = 0;
    }

    public static TransferLocalVarToStaticGlobal getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brotherStatement, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        Expression targetLiteral = (Expression) targetNode;  // Notice check, hence, targetNode is literal.
        TypeDeclaration clazz = TypeWrapper.getClassOfNode(srcNode);
        String newVarName = "LOCAL_STATIC" + varCounter++;
        SimpleName newVar = ast.newSimpleName(newVarName);
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        newVdFragment.setName(newVar);
        newVdFragment.setInitializer((Expression) ASTNode.copySubtree(ast, targetLiteral));
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(newVdFragment);
        fieldDeclaration.setType(TypeWrapper.checkLiteralType(ast, targetLiteral));
        fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
        fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertFirst(fieldDeclaration, null);
        astRewrite.replace(targetLiteral, newVar, null);
        wrapper.getPriorNodes().add(fieldDeclaration);
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode srcNode) {
        List<ASTNode> nodes = new ArrayList<>();
        TypeDeclaration clazz = TypeWrapper.getClassOfNode(srcNode);
        if(clazz == null || clazz.isInterface()) {
            return nodes;
        }
        List<ASTNode> subNodes;
        if(LoopStatement.isLoopStatement(srcNode)) {
            LoopStatement loop = new LoopStatement(srcNode);
            if(loop.getBody() instanceof Block) {
                subNodes = TypeWrapper.getChildrenNodes((List<ASTNode>)((Block) loop.getBody()).statements());
            } else {
                subNodes = TypeWrapper.getChildrenNodes(loop.getBody());
            }
        } else {
            subNodes = TypeWrapper.getChildrenNodes(srcNode);
        }
        for(int i = 0; i < subNodes.size(); i++) {
            ASTNode subNode = subNodes.get(i);
            if(TypeWrapper.isLiteral(subNode)) {
                nodes.add(subNode);
            }
        }
        return nodes;
    }

}
