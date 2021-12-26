
public class Foo {
    private SortedSet<LocalizedMessage> getFilteredMessages(
            String fileName, FileContents fileContents, DetailAST rootAST) {
        final SortedSet<LocalizedMessage> result = new TreeSet<>(messages);
        for (LocalizedMessage element : messages) {
            final TreeWalkerAuditEvent event =
                    new TreeWalkerAuditEvent(fileContents, fileName, element, rootAST);
            for (TreeWalkerFilter filter : filters) {
                if (!filter.accept(event)) {
                    result.remove(element);
                    break;
                }
            }
        }
        return result;
    }
}
        