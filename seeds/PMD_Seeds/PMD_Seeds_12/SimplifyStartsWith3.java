
public class SimplifyStartsWith {
    private static boolean isImmediatelyFollowedByText(DetailNode tag) {
        final DetailNode nextSibling = JavadocUtils.getNextSibling(tag);
        return nextSibling.getType() == JavadocTokenTypes.NEWLINE
                || nextSibling.getType() == JavadocTokenTypes.EOF
                || nextSibling.getText().startsWith(" ");
    }
}
        