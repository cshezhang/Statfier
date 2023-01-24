package org.detector.analysis;

import org.eclipse.jdt.core.dom.Statement;

import java.util.ArrayList;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2021/10/11 1:32 PM
 */
public class BasicBlock {

    private ArrayList<Statement> statements;
    private ArrayList<BasicBlock> succBlocks;
    private ArrayList<BasicBlock> prevBlocks;

    public BasicBlock(ArrayList<Statement> statements) {
        this.statements = statements;
        this.prevBlocks = new ArrayList<>();
        this.succBlocks = new ArrayList<>();
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
    }

    public ArrayList<Statement> getStatements() {
        return this.statements;
    }

    public void addSucc(BasicBlock succBlock) {
        this.succBlocks.add(succBlock);
    }

    public void addPrev(BasicBlock prevBlock) {
        this.prevBlocks.add(prevBlock);
    }

    public ArrayList<BasicBlock> getPrevBlocks() {
        return this.prevBlocks;
    }

    public ArrayList<BasicBlock> getSuccBlocks() {
        return this.succBlocks;
    }

}
