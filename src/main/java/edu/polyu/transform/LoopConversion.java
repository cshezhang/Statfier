package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.analysis.LoopStatement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Transfer a Loop to Lambda Expression Loop
 * @Author: Vanguard
 * @Date: 2022-02-26 15:42
 */
public class LoopConversion extends Transform {
    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brotherStatement, ASTNode oldStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        LoopStatement loopStatement = new LoopStatement(oldStatement);
        loopStatement.getBody();
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(LoopStatement.isLoopStatement(node)) {
            nodes.add(node);
        }
        return nodes;
    }

}
