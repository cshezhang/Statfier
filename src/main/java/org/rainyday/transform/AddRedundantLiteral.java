package org.rainyday.transform;

import org.rainyday.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static org.rainyday.analysis.TypeWrapper.getChildrenNodes;

/**
 * @Description: The mutant to add redundant literal
 * @Author: Vanguard
 * @Date: 2021-08-20 21:15
 */
public class AddRedundantLiteral extends Transform {

    private static AddRedundantLiteral addRedundantLiteral = new AddRedundantLiteral();

    private AddRedundantLiteral() {}

    public static AddRedundantLiteral getInstance() {
        return addRedundantLiteral;
    }

    /*
    int a = 10; -> int a = 10 + 1 - 1;
     */

    @Override
    public boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode brotherStatement, ASTNode sourceStatement) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        NumberLiteral targetLiteral = (NumberLiteral) targetNode;
        if(targetLiteral.getToken().toLowerCase().contains("0x") && targetLiteral.getToken().contains(".")) {
            return false;
        }
        InfixExpression newLeft = ast.newInfixExpression();
        String value2add;
        if(targetLiteral.getToken().contains(".")) {
            value2add = "1.0";
        } else {
            value2add = "1";
        }
        newLeft.setLeftOperand(ast.newNumberLiteral(value2add));
        newLeft.setOperator(InfixExpression.Operator.PLUS);
        newLeft.setRightOperand((Expression) ASTNode.copySubtree(ast, targetLiteral));
        InfixExpression newRight = ast.newInfixExpression();
        newRight.setLeftOperand(newLeft);
        newRight.setRightOperand(ast.newNumberLiteral(value2add));
        newRight.setOperator(InfixExpression.Operator.MINUS);
        ParenthesizedExpression newRightExpression = ast.newParenthesizedExpression();
        newRightExpression.setExpression(newRight);
        astRewrite.replace(targetLiteral, newRightExpression, null);
        return true;
    }

    @Override
    public List<ASTNode> check(TypeWrapper wrapper, ASTNode statement) {
        List<ASTNode> nodes = new ArrayList<>();
        List<ASTNode> subNodes = TypeWrapper.getChildrenNodes(statement);
        for(int i = 0; i < subNodes.size(); i++) {
            ASTNode node = subNodes.get(i);
            if(node instanceof NumberLiteral) {
                nodes.add(node);
            }
        }
        return nodes;
    }
}
