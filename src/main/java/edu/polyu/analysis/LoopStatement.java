package edu.polyu.analysis;


import org.eclipse.jdt.core.dom.*;

/**
 * Description: This class is used to process and unify loop-related statements
 * Author: Austin Zhang
 * Date: 2021/10/11 2:02 下午
 */
public class LoopStatement {

    private byte tag;
    private Statement loopStatement;
    private Statement body;

    public LoopStatement(Statement loopStatement) {
        this.tag = -1;
        if(loopStatement instanceof ForStatement) {
            this.tag = 0;
            this.loopStatement = loopStatement;
            this.body = ((ForStatement) loopStatement).getBody();
        }
        if(loopStatement instanceof WhileStatement) {
            this.tag = 1;
            this.loopStatement = loopStatement;
            this.body = ((WhileStatement) loopStatement).getBody();
        }
        if(loopStatement instanceof DoStatement) {
            this.tag = 2;
            this.loopStatement = loopStatement;
            this.body = ((DoStatement) loopStatement).getBody();
        }
        if(loopStatement instanceof EnhancedForStatement) {
            this.tag = 3;
            this.loopStatement = loopStatement;
            this.body = ((EnhancedForStatement) loopStatement).getBody();
        }
        if(tag == -1) {
            System.err.println("This is not a Loop Statement!");
        }
    }

    public Statement getLoopStatement() {
        return this.loopStatement;
    }

    public Statement getBody() {
        return this.body;
    }

    public static boolean isLoopStatement(Statement statement) {
        if (statement instanceof EnhancedForStatement || statement instanceof ForStatement || statement instanceof WhileStatement || statement instanceof DoStatement) {
            return true;
        }
        return false;
    }

}
