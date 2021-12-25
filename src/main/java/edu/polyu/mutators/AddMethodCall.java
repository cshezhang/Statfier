package edu.polyu.mutators;

import edu.polyu.Mutator;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.Util.*;

public class AddMethodCall extends Mutator {

    private static AddMethodCall addMethodCall = new AddMethodCall();
    private AddMethodCall() {}

    public static AddMethodCall getInstance() {
        return addMethodCall;
    }

    @Override
    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement) {
        List<ASTNode> nodes = getChildrenNodes(sourceStatement);
        List<ASTNode> candidateNodes = new ArrayList<>();
        for(ASTNode node : nodes) {
            if(checkExpressionLiteral(node)) {
                candidateNodes.add(node);
            }
        }
        ASTNode targetNode = candidateNodes.get(random.nextInt(candidateNodes.size()));

        return false;
    }

    @Override
    public boolean check(Statement statement) {
        List<ASTNode> nodes = getChildrenNodes(statement);
        for(ASTNode astNode : nodes) {
            if(checkExpressionLiteral(astNode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getIndex() {
        return 5;
    }
}
