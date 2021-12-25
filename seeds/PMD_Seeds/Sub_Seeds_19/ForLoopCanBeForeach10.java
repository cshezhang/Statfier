
class AbstractApexNode {
    Node[] children;

    public Object childrenAccept(ApexParserVisitor visitor, Object data) {
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                @SuppressWarnings("unchecked")
                // we know that the children here are all ApexNodes
                ApexNode<T> apexNode = (ApexNode<T>) children[i];
                apexNode.jjtAccept(visitor, data);
            }
        }
        return data;
    }
}
        