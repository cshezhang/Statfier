
public class InsufficientStringBufferDeclaration {
    protected void appendHintMessage(StringBuilder msg) {
        msg.append("A very long text");
        msg.append("that is longer than 16 characters");
    }
}
        