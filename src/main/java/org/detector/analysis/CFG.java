package org.detector.analysis;

import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2021/10/11 1:32 PM
 */
public class CFG {

    private BasicBlock entry;
    private BasicBlock exit;
    private Set<BasicBlock> blocks;
    private Set<CFGEdge> edges;

    public CFG() {
        this.blocks = new HashSet<>();
        this.edges=  new HashSet<>();
    }

    public void addEdge(CFGEdge edge) {
        this.edges.add(edge);
        this.blocks.add(edge.getStartNode());
        this.blocks.add(edge.getEndNode());
    }

    public static List<BasicBlock> splitBasicBlocks(MethodDeclaration method) {
        List<BasicBlock> blocks = new ArrayList<>();
        Set<Statement> leadingStatements = new HashSet<>();
        leadingStatements.add((Statement) method.getBody().statements().get(0));
        for(int i = 1; i < method.getBody().statements().size(); i++) {
            Statement statement = (Statement) method.getBody().statements().get(i);
            if(statement instanceof IfStatement) {
                leadingStatements.add(statement);
                IfStatement ifStatement = (IfStatement) statement;
                leadingStatements.add(ifStatement.getThenStatement());
                leadingStatements.add(ifStatement.getElseStatement());
                if(i + 1 < method.getBody().statements().size()) {
                    leadingStatements.add((Statement) method.getBody().statements().get(i + 1));
                }
            }
            if(statement instanceof WhileStatement) {
                leadingStatements.add(statement);
                leadingStatements.add(((WhileStatement) statement).getBody());
                leadingStatements.add((Statement) method.getBody().statements().get(i + 1));
                if(i + 1 < method.getBody().statements().size()) {
                    leadingStatements.add((Statement) method.getBody().statements().get(i + 1));
                }
            }
            if(statement instanceof ForStatement) {
                leadingStatements.add(statement);
                leadingStatements.add(((ForStatement) statement).getBody());
                leadingStatements.add((Statement) method.getBody().statements().get(i + 1));
                if(i + 1 < method.getBody().statements().size()) {
                    leadingStatements.add((Statement) method.getBody().statements().get(i + 1));
                }
            }
            if(statement instanceof DoStatement) {
                leadingStatements.add(statement);
                leadingStatements.add(((DoStatement) statement).getBody());
                leadingStatements.add((Statement) method.getBody().statements().get(i + 1));
                if(i + 1 < method.getBody().statements().size()) {
                    leadingStatements.add((Statement) method.getBody().statements().get(i + 1));
                }
            }
        }
        for(int i = 0; i < method.getBody().statements().size(); i++) {
            
        }
        return blocks;
    }

}
