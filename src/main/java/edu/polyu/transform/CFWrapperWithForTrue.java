package edu.polyu.transform;


import edu.polyu.analysis.ASTWrapper;
import edu.polyu.util.Util;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Util.getAllStatements;
import static edu.polyu.util.Util.getDirectMethodOfStatement;

// Control flow wrapper based on for-loop
public class CFWrapperWithForTrue extends Transform {

    private static final CFWrapperWithForTrue instance = new CFWrapperWithForTrue();

    private static int varCounter;

    private CFWrapperWithForTrue() {
        varCounter = 0;
    }

    public static CFWrapperWithForTrue getInstance() {
        return instance;
    }

    @Override
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode brother, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        ASTNode parNode = srcNode.getParent();
        if(parNode instanceof Block) {
            if(((Block) parNode).statements().size() == 1) {
                return false;
            }
        }
        ForStatement newForStatement = ast.newForStatement();
        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
        String controlVar = String.format("cfwwft%d", varCounter++);
        newVdFragment.setName(ast.newSimpleName(controlVar));
        newVdFragment.setInitializer(ast.newNumberLiteral("0"));

        VariableDeclarationExpression newVdExpression = ast.newVariableDeclarationExpression(newVdFragment);
        newVdExpression.setType(ast.newPrimitiveType(PrimitiveType.INT));
        newForStatement.initializers().add(newVdExpression);

        InfixExpression infixExpression = ast.newInfixExpression();
        infixExpression.setLeftOperand(ast.newSimpleName(controlVar));
        infixExpression.setOperator(InfixExpression.Operator.LESS);
        infixExpression.setRightOperand(ast.newNumberLiteral("1"));
        newForStatement.setExpression(infixExpression);

        PostfixExpression postfixExpression = ast.newPostfixExpression();
        postfixExpression.setOperand(ast.newSimpleName(controlVar));
        postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
        newForStatement.updaters().add(postfixExpression);

        Block newForBodyBlock = ast.newBlock();
        Statement newStatement = (Statement) ASTNode.copySubtree(ast, srcNode);
        newForBodyBlock.statements().add(newStatement);
        newForStatement.setBody(newForBodyBlock);
        astRewrite.replace(srcNode, newForStatement, null);
        return true;
    }

    public static boolean InitCheck(ASTNode node) {
        if(node instanceof ExpressionStatement && ((ExpressionStatement) node).getExpression() instanceof Assignment) {
            Assignment assignment = (Assignment) ((ExpressionStatement) node).getExpression();
            if(assignment.getLeftHandSide() instanceof SimpleName) {
                String varName = ((SimpleName) assignment.getLeftHandSide()).getIdentifier();
                MethodDeclaration method = getDirectMethodOfStatement(node);
                List<Statement> statements = getAllStatements(method.getBody().statements());
                for (Statement statement : statements) {
                    if (statement instanceof VariableDeclarationStatement) {
                        VariableDeclarationFragment vdFragment = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
                        if (vdFragment.getName().getIdentifier().equals(varName) && vdFragment.getInitializer() == null) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if(!InitCheck(node)) {
            return nodes;
        }
        if(node instanceof VariableDeclarationStatement || node instanceof FieldDeclaration ||
            node instanceof MethodDeclaration || node instanceof ReturnStatement || node instanceof SuperConstructorInvocation) {
            return nodes;
        }
        nodes.add(node);
        return nodes;
    }

}
