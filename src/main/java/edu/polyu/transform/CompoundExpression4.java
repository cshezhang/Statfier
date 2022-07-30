package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.analysis.TypeWrapper.getChildrenNodes;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/7/28 11:02
 */
public class CompoundExpression4 extends Transform {

    private static CompoundExpression4 instance = new CompoundExpression4();

    private CompoundExpression4() {}

    public static CompoundExpression4 getInstance() {
        return instance;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        List<ASTNode> subNodes = getChildrenNodes(node);
        for (ASTNode subNode : subNodes) {
            if (subNode instanceof NumberLiteral) {
                nodes.add(subNode);
            }
        }
        return nodes;
    }

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode broNode, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        NumberLiteral targetLiteral = (NumberLiteral) targetNode;
        ParenthesizedExpression newParenthesizedExpression = ast.newParenthesizedExpression();
        InfixExpression newInfixExpression = ast.newInfixExpression();
        newInfixExpression.setLeftOperand((NumberLiteral) ASTNode.copySubtree(ast, targetLiteral));
        newInfixExpression.setRightOperand(ast.newNumberLiteral("0"));
        newInfixExpression.setOperator(InfixExpression.Operator.MINUS);
        newParenthesizedExpression.setExpression(newInfixExpression);
        astRewrite.replace(targetLiteral, newParenthesizedExpression, null);
        return true;
    }

}
