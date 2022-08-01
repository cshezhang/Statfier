
public class Foo {
    public Object visit(ASTFieldDeclaration node, Object data) {
        ASTClassOrInterfaceDeclaration cl = node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        if (cl != null && node.getVariableName().toLowerCase().equals(cl.getImage().toLowerCase())) {
            addViolation(data, node);
        }
        return data;
    }
}
        