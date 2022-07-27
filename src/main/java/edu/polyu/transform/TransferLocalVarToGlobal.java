package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.analysis.TypeWrapper.checkLiteralType;
import static edu.polyu.analysis.TypeWrapper.getClassOfNode;
import static edu.polyu.analysis.TypeWrapper.isLiteral;
import static edu.polyu.analysis.TypeWrapper.getChildrenNodes;
import static edu.polyu.analysis.TypeWrapper.getDirectMethodOfNode;

public class TransferLocalVarToGlobal extends Transform {

    private static int varCounter;
    private static TransferLocalVarToGlobal instance = new TransferLocalVarToGlobal();

    private TransferLocalVarToGlobal() {
        varCounter = 0;
    }

    public static TransferLocalVarToGlobal getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brotherStatement, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        Expression literalNode = (Expression) targetNode;
        TypeDeclaration clazz = getClassOfNode(srcNode);
        String newVarName = "t2g" + varCounter++;
        SimpleName newVar = ast.newSimpleName(newVarName);
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        newVdFragment.setName(newVar);
        newVdFragment.setInitializer((Expression) ASTNode.copySubtree(ast, literalNode));
        FieldDeclaration newFieldDeclaration = ast.newFieldDeclaration(newVdFragment);
        newFieldDeclaration.setType(checkLiteralType(ast, literalNode));
        newFieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertFirst(newFieldDeclaration, null);
        astRewrite.replace(literalNode, newVar, null);
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode srcNode) {
        List<ASTNode> nodes = new ArrayList<>();
        MethodDeclaration method = getDirectMethodOfNode(srcNode);
        if (method != null) {
            for (ASTNode modifier : (List<ASTNode>) method.modifiers()) {
                if(modifier instanceof Modifier) {
                    if(((Modifier) modifier).getKeyword().toString().contains("static")) {
                        return nodes;
                    }
                }
            }
        } else {
            ASTNode parNode = srcNode.getParent();
            while(parNode != null) {
                if(parNode instanceof Initializer) {
                    return nodes;
                }
                parNode = parNode.getParent();
            }
        }
        List<ASTNode> subNodes = getChildrenNodes(srcNode);
        for (int i = 0; i < subNodes.size(); i++) {
            ASTNode subNode = subNodes.get(i);
            if (isLiteral(subNode)) {
                nodes.add(subNode);
            }
        }
        return nodes;
    }

}
