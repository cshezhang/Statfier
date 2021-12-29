package edu.polyu.mutators;

import edu.polyu.Mutator;
import edu.polyu.Util;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.Util.getChildrenNodes;
import static edu.polyu.Util.mutantCounter;
import static edu.polyu.Util.random;

public class TransferLocalVarToGlobal extends Mutator {

    private static int varCounter;
    private static TransferLocalVarToGlobal instance = new TransferLocalVarToGlobal();

    private TransferLocalVarToGlobal() {
        varCounter = 0;
    }

    public static TransferLocalVarToGlobal getInstance() {
        return instance;
    }

    @Override
    public int getIndex() {
        return 11;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
        List<ASTNode> subNodes = getChildrenNodes(sourceStatement);
        List<ASTNode> literalNodes = new ArrayList<>();
        for(int i = 0; i < subNodes.size(); i++) {
            ASTNode node = subNodes.get(i);
            if(Util.checkExpressionLiteral(node)) {
                literalNodes.add(node);
            }
        }
        int randomIndex = random.nextInt(literalNodes.size());
        Expression targetNode = (Expression) literalNodes.get(randomIndex);
        TypeDeclaration clazz = Util.getTypeOfStatement(sourceStatement);
        String newVarName = "t2g" + varCounter++;
        SimpleName newVar = ast.newSimpleName(newVarName);
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        newVdFragment.setName(newVar);
        newVdFragment.setInitializer(targetNode);
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(newVdFragment);
        fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertFirst(fieldDeclaration, null);
        astRewrite.replace(targetNode, newVar, null);
        return true;
    }

    @Override
    public boolean check(Statement statement) {
        List<ASTNode> nodes = getChildrenNodes(statement);
        for(int i = 0; i < nodes.size(); i++) {
            if(Util.checkExpressionLiteral(nodes.get(i))) {
                return true;
            }
        }
        return false;
    }

}
