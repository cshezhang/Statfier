package edu.polyu.analysis;

public class CFGEdge {

    private BasicBlock startNode, endNode;

    public CFGEdge(BasicBlock startNode, BasicBlock endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public BasicBlock getStartNode() {
        return startNode;
    }

    public BasicBlock getEndNode() {
        return endNode;
    }

}
