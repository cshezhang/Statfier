package org.rainyday.analysis;


import net.sf.saxon.instruct.While;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.compiler.ast.Literal;

/**
 * Description: This class is used to process and unify loop-related statements
 * Author: Vanguard
 * Date: 2021/10/11 2:02 PM
 */
public class LoopStatement {

    private byte tag;
    private Statement loopStatement;
    private Expression expression;
    private Statement body;

    public LoopStatement(ASTNode node) {
        this.tag = -1;
        if(node instanceof ForStatement) {
            this.tag = 0;
            this.loopStatement = (ForStatement) node;
            this.expression = ((ForStatement) node).getExpression();
            this.body = ((ForStatement) this.loopStatement).getBody();
        }
        if(node instanceof WhileStatement) {
            this.tag = 1;
            this.loopStatement = (WhileStatement) node;
            this.expression = ((WhileStatement) node).getExpression();
            this.body = ((WhileStatement) this.loopStatement).getBody();
        }
        if(node instanceof DoStatement) {
            this.tag = 2;
            this.loopStatement = (DoStatement) node;
            this.expression = ((DoStatement) node).getExpression();
            this.body = ((DoStatement) this.loopStatement).getBody();
        }
        if(node instanceof EnhancedForStatement) {
            this.tag = 3;
            this.loopStatement = (EnhancedForStatement) node;
            this.expression = ((EnhancedForStatement) node).getExpression();
            this.body = ((EnhancedForStatement) this.loopStatement).getBody();
        }
        if(tag == -1) {
            System.err.println("This is not a Loop Statement!");
        }
    }

    public Expression getExpression() {
        return this.expression;
    }

    public boolean isReachable() {
        if(this.expression instanceof BooleanLiteral && !((BooleanLiteral) this.expression).booleanValue()) {
            return false;
        }
        return true;
    }

    public Statement getBody() {
        return this.body;
    }

    public static boolean isLoopStatement(ASTNode node) {
        if (node instanceof EnhancedForStatement || node instanceof ForStatement
                || node instanceof WhileStatement || node instanceof DoStatement) {
            return true;
        }
        return false;
    }

    public boolean isForStatement() {
        return this.loopStatement instanceof ForStatement;
    }

    public boolean isWhileStatement() {
        return this.loopStatement instanceof WhileStatement;
    }

    public boolean isDoStatement() {
        return this.loopStatement instanceof DoStatement;
    }

    public boolean isEnhancedForStatement() {
        return this.loopStatement instanceof EnhancedForStatement;
    }

    @Override
    public String toString() {
        return this.loopStatement.toString();
    }

}
