
public class ExceptionAsFlowControlRule extends AbstractJavaRule {

    @Override
    public Object visit(ASTThrowStatement node, Object data) {
        ASTTryStatement parent = node.getFirstParentOfType(ASTTryStatement.class);
        if (parent == null) {
            return data;
        }
        for (parent = parent.getFirstParentOfType(ASTTryStatement.class); parent != null; parent = parent
                .getFirstParentOfType(ASTTryStatement.class)) {

            List<ASTCatchStatement> list = parent.findDescendantsOfType(ASTCatchStatement.class);
            // ...
        }
        return data;
    }
}
        