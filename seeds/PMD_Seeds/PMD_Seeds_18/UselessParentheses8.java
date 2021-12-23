
public class Foo {
    private boolean isSimpleReturn(Node node) {
        return
            ( node instanceof ASTReturnStatement ) // rule triggeres here
            &&
            ( node.getNumChildren() == 0 );     // and here
    }
    public String getLocalName() {
        int colonIndex = name.indexOf(':');
        return (colonIndex >= 0)                // and here
                ? name.substring(colonIndex + 1)
                : name;
    }
    public void viewerModelChanged(ViewerModelEvent e) {
        int startOffset =
          // and here for the following parentheses once
          (sourceCodeArea.getLineStartOffset(node.getBeginLine() - 1) +
          node.getBeginColumn()) - 1;
    }
}
        